package com.example.SamvaadProject.attendancepackage;

import com.example.SamvaadProject.admissionpackage.AdmissionDTO;
import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.attendance_view.AttendanceView;
import com.example.SamvaadProject.attendance_view.AttendanceViewDTO;
import com.example.SamvaadProject.attendance_view.ShowAttendanceDTO;
import com.example.SamvaadProject.attendance_view.attendanceview_repo;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.coursepackage.CourseRepository;
import com.example.SamvaadProject.studentbatchpackage.StudentBatchMap;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import com.example.SamvaadProject.usermasterpackage.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class AttendanceController {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    BatchMasterRepository batchMasterRepository;

    @Autowired
    AdmissionRepository admissionRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    attendanceview_repo attendanceviewRepo;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/facultyattendance")
    public  String Attendance(Model model, HttpSession session)
    {
        List<BatchMaster> batchlist=batchMasterRepository.findByFacultyId(2);
        model.addAttribute("Batches",batchlist);
        session.setAttribute("Batches",batchlist);
        LocalDate date=LocalDate.now();
        model.addAttribute("selectedDate",date);
        return "attendanceForm";
    }

//    Get Student for attendance
    @GetMapping("/getstudentsforattendance/{batchId}")
    @ResponseBody
    public List<AdmissionDTO> getStudent(@PathVariable("batchId") Long batchId){
       return admissionRepository.findByBatchId(batchId)
                .stream()
                .map(adm->new AdmissionDTO(adm.getAdmissionId(),adm.getUserMaster().getFullName()))
                .toList();

//        return attendanceviewRepo.findByBatchId(batchId.longValue())
//                .stream()
//                .map(av->new AttendanceViewDTO(av.getAdmission_id(),av.getFull_name()))
//                .toList();
    }

    @GetMapping("/coursePage")
    public String getId(@RequestParam("batchId") Long batchId,Model model,HttpSession session)
    {
        List<BatchMaster> batchList= (List<BatchMaster>) session.getAttribute("Batches");

        session.setAttribute("batchid",batchId);
        session.setAttribute("Batches",batchList);
        List<AttendanceView> attendanceView=attendanceviewRepo.findByBatchId(batchId.longValue());
        session.setAttribute("Studentname",attendanceView);

        LocalDate date=LocalDate.now();
        model.addAttribute("selectedDate",date);
        model.addAttribute("Studentname",attendanceView);
        model.addAttribute("selectedBatchId",batchId);
        model.addAttribute("Batches",batchList);
        System.out.println("Select Wali "+batchId);
        return "attendanceForm";
    }

    @PostMapping("/MarkedAttendance")
    public String attendance(Model model,
                             HttpSession session,
                             @RequestParam("attendance") @DateTimeFormat(pattern = "yyyy-MM-dd") Date attendanceDate,
                             @RequestParam(value = "attendanceStatus",required = false) List<String> attendanceStatus,
                             @RequestParam("batchSelect")Long batchId,
                             RedirectAttributes redirectAttributes) {

//        List<AttendanceView> admissionidList=attendanceviewRepo.findByBatchId(batchId); // admission ids of student ;
        List<AdmissionMaster>admissionidList=admissionRepository.findByBatchId(batchId);
//        System.out.println("Total AdIDs ");
//        for(AttendanceView view : admissionidList ){
//            System.out.print(", "+view.getAdmission_id());
//        }
//        System.out.println("To be present "+attendanceStatus);

        for( int i=0;i<admissionidList.size();i++)
        {

            boolean isPresent =  attendanceStatus != null && attendanceStatus.contains(String.valueOf(admissionidList.get(i).getAdmissionId()));
            System.out.println("Admission Id "+admissionidList.get(i)+" isPresent "+isPresent);
//            check date is already exist or not
            Optional<AttendanceMaster> existingAttendance=attendanceRepository.findByAdmission_AdmissionIdAndAttendanceDate(String.valueOf(admissionidList.get(i).getAdmissionId()),attendanceDate);
            if (existingAttendance.isPresent()) {
                // Attendance already marked; send message to view
                redirectAttributes.addAttribute("attendanceAlreadyErrorMessage", true);//"Attendance is already marked.");
                return "redirect:/faculty/dashboard";
            }


            AttendanceMaster attendanceMaster=new AttendanceMaster();

//            System.out.println(attendanceDate);
//            System.out.println(isPresent ? "Present" : "Absent");
//            System.out.println(admissionidList.get(i).getAdmission_id());
            attendanceMaster.setAttendanceDate(attendanceDate);
            attendanceMaster.setStatus( isPresent ? "Present" : "Absent");

            // Create an AdmissionMaster object and set its ID
            AdmissionMaster admission = new AdmissionMaster();
            admission.setAdmissionId(String.valueOf(admissionidList.get(i).getAdmissionId()));

            attendanceMaster.setAdmission(admission);
//            System.out.println(admission);
            attendanceRepository.save(attendanceMaster);
        }
        redirectAttributes.addAttribute("attendanceMarked",true);
        return "redirect:/faculty/dashboard";
    }


    @GetMapping("/UpdateAttendance")
    public String Update(HttpSession session,Model model)
    {
        //Find All Batch Name By FacultyId ;
        List<BatchMaster> AllBatch=batchMasterRepository.findByFacultyId(2);
        if(!AllBatch.isEmpty()) {
            model.addAttribute("Batches", AllBatch);
            session.setAttribute("Batches",AllBatch);
        }
        else {
            model.addAttribute("Batches", null);
        }
        model.addAttribute("CurrDate",null);
        return "attendanceForm";
    }

    @GetMapping("/getstudentupdate/{date}/{batchId}")
    @ResponseBody
    public List<AttendanceDTO> getStudentForUpdate(@PathVariable("date")String date,@PathVariable("batchId")Long batchId){
        System.out.println("Entered Inside the update student attendance method!");
        java.sql.Date currdate= java.sql.Date.valueOf(date); //Convert Date

        List<AdmissionMaster> admissionId=admissionRepository.findByBatchId(batchId);
//        System.out.println("BATCH Id"+batchId);

        List<String> admissionidList=new ArrayList<>(); //Store all admission Ids in This List
        for(int i=0;i<admissionId.size();i++)
        {
            admissionidList.add(admissionId.get(i).getAdmissionId());
//            System.out.println(admissionId.get(i).getAdmission_id().toString());
        }

        return attendanceRepository.findByAttendanceDateAndAdmissionIds(currdate,admissionidList)
                .stream()
                .map(selectedStudent->new AttendanceDTO(selectedStudent.getAdmission().getAdmissionId(),
                        selectedStudent.getAdmission().getUserMaster().getFullName(),
                        selectedStudent.getStatus(),selectedStudent.getAttendanceId()))
                .toList();

    }

    @GetMapping("/selectDate")
    public String SelectedDate(HttpSession session,Model model,@RequestParam( value ="selectedDate" ,required = false) String selectedDate,@RequestParam(value = "batchId" ,required = false) Long batchId)
    {
        java.sql.Date currdate= java.sql.Date.valueOf(selectedDate); //Convert Date

        List<AttendanceView> admissionId=attendanceviewRepo.findByBatchId(batchId);
        System.out.println("BATCH Id"+batchId);

        List<String> admissionidList=new ArrayList<>(); //Store all admission Ids in This List
        for(int i=0;i<admissionId.size();i++)
        {
            admissionidList.add(admissionId.get(i).getAdmission_id().toString());
            System.out.println(admissionId.get(i).getAdmission_id().toString());
        }
        System.out.println("List "+admissionidList);
        System.out.println(selectedDate);

        //Match Ids and date from the attendance table;
        List<AttendanceMaster> attendanceMasterList=attendanceRepository.findByAttendanceDateAndAdmissionIds(currdate,admissionidList);
        if(!attendanceMasterList.isEmpty()) {
            model.addAttribute("SelectedStudent", attendanceMasterList);
            session.setAttribute("SelectedStudent", attendanceMasterList);
        }
        else
            model.addAttribute("SelectedStudent",null);

        return "attendanceForm";
    }

    @PostMapping("/UpdateAttendance")
    public String Updated(Model model,
                          HttpSession session,
                          @RequestParam("attendanceId") List<Long> attendanceId,
                          @RequestParam("status") List<String> status,
                          @RequestParam("attendanceDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date selectedDate,
                          RedirectAttributes redirectAttributes)
    {

        //Update Attendance by Attendance id
        for(int i=0;i<attendanceId.size();i++)
        {
            System.out.println(attendanceId.get(i));
            Optional<AttendanceMaster> attendanceMaster=attendanceRepository.findById(attendanceId.get(i));
            if(attendanceMaster.isPresent()) {
                AttendanceMaster master=attendanceMaster.get();
//                master.setAttendanceDate(selectedDate);
                master.setStatus(status.get(i));
                attendanceRepository.save(master);
            }
            else {
                System.out.println("Attendance not found for ID: " + attendanceId.get(i));
            }
        }
        redirectAttributes.addAttribute("attendanceUpdated",true);
        return "redirect:/faculty/dashboard";
    }

    @GetMapping("/ShowAttendance")
    public String DeletedAttendance(HttpSession session,Model model) {
//        List<AttendanceView> AllBatch= (List<AttendanceView>) session.getAttribute("Batches"); //Again Get All Batches
//        Long batchid= (Long) session.getAttribute("SelectedBatchId"); //Get selected Batch ID;
//        Date currDate= (Date) session.getAttribute("CurrDate"); // Get Selected DaTe ;
//        List<String>attendanceidList= (List<String>) session.getAttribute("admissionList");//Get AdmissionIds from session
//        List<String>attendanceidList1= (List<String>) session.getAttribute("admissionList");//Get AdmissionIds from session
//
//        List<AttendanceMaster> attendanceMaster=attendanceRepository.findByAttendanceDateAndAdmissionIds(selectedDate,attendanceidList1);//Match Ids and date from the attendance table;
//
//        model.addAttribute("SelectedStudent", attendanceMaster);
//        model.addAttribute("SelectedBatchId",batchid);
//        model.addAttribute("CurrDate",currDate);
//        model.addAttribute("Batches",AllBatch);
//        model.addAttribute("UpdateAttendance","Update Attendance");
        List<BatchMaster> AllBatch=batchMasterRepository.findByFacultyId(2);
        if(!AllBatch.isEmpty()) {
            model.addAttribute("Batches", AllBatch);
            session.setAttribute("Batches",AllBatch);
        }
        else {
            model.addAttribute("Batches", null);
        }
        model.addAttribute("CurrDate",null);
        return "attendanceForm";
    }

    @GetMapping("/SearchShowAttendance")
    public String ShowAttendance(HttpSession session,Model model,@RequestParam(value = "selectedDate",required = false) String selectedDate,@RequestParam(value = "batchId" ,required = false) Long batchId)
    {
        java.sql.Date currdate= java.sql.Date.valueOf(selectedDate);
        List<BatchMaster> batchlist= (List<BatchMaster>) session.getAttribute("Batches");
        if(batchlist.isEmpty())
        {
            model.addAttribute("BatchError","Batch Id Not Present");
            model.addAttribute("Batches",null);
            return "attendanceForm";
        }
        List<AttendanceView>ids=attendanceviewRepo.findByBatchId(batchId);

        List<String> admissionids=new ArrayList<>();
        for(int i=0;i<ids.size();i++)
        {
            admissionids.add(ids.get(i).getAdmission_id().toString());
        }
        System.out.println(admissionids);

        List<AttendanceMaster>attendanceMasterList=attendanceRepository.findByAttendanceDateAndAdmissionIds(currdate,admissionids);
        if(!attendanceMasterList.isEmpty())
        {
            session.setAttribute("SelectedStudent",attendanceMasterList);
            model.addAttribute("SelectedStudent",attendanceMasterList);
        }
        else
        {
            model.addAttribute("SelectedStudent",null);
        }
        session.setAttribute("currDate",selectedDate);
        session.setAttribute("SelectedBatchId",batchId);
        session.setAttribute("CurrDate",currdate);
        session.setAttribute("Batches",batchlist);

        model.addAttribute("SelectedBatchId",batchId);
        model.addAttribute("CurrDate",currdate);
        model.addAttribute("Batches",batchlist);
        return "attendanceForm";
    }

    @GetMapping("/DeleteAttendance")
    public String delete(HttpSession session,Model model,@RequestParam("attendanceId") long attendanceId) {

        String date= (String) session.getAttribute("currDate");
        java.sql.Date currdate= java.sql.Date.valueOf(date);
        Long batchId= (Long) session.getAttribute("SelectedBatchId");
        if( currdate == null || batchId == null)
        {
            model.addAttribute("Error","Nor Found Ids");
            return "attendanceForm";
        }
        System.out.println(currdate);
        System.out.println(batchId);
        attendanceRepository.deleteById(attendanceId);//Delete Attendance ;
        System.out.println("Deleted Id"+attendanceId);
        List<BatchMaster> batchlist= (List<BatchMaster>) session.getAttribute("Batches");

        List<AttendanceView>ids=attendanceviewRepo.findByBatchId(batchId);

        List<String> admissionids=new ArrayList<>();
        for(int i=0;i<ids.size();i++)
        {
            admissionids.add(ids.get(i).getAdmission_id().toString());
        }
        System.out.println(admissionids);
        List<AttendanceMaster>attendanceMasterList=attendanceRepository.findByAttendanceDateAndAdmissionIds(currdate,admissionids);
        if(!attendanceMasterList.isEmpty())
        {
            session.setAttribute("SelectedStudent",attendanceMasterList);
            model.addAttribute("SelectedStudent",attendanceMasterList);
        }
        else
        {
            model.addAttribute("SelectedStudent",null);
        }
        model.addAttribute("SelectedBatchId",batchId);
        model.addAttribute("CurrDate",currdate);
        model.addAttribute("Batches",batchlist);
        model.addAttribute("DeleteAttendance","Delete Attendance");
        return "attendanceForm";
    }
//<--------------------------------  Student Part Start From Here     --------------------------------------->

    Long totalAttendance=0L;
    Long present=0L;
    @GetMapping("/studentattendance")
    public String StudentAttendance(Model model,HttpSession session)
    {
        System.out.println("Inside the Student Attendance Method");
        List<AdmissionMaster> Data=admissionRepository.findByUserMaster(1L);
        List<Long> courseIds=new ArrayList<>();
        List<String> AdmissionIds=new ArrayList<>();
        for(int i=0;i<Data.size();i++)
        {
            courseIds.add(Data.get(i).getCourse().getCourseId());
            AdmissionIds.add(Data.get(i).getAdmissionId());
        }
        session.setAttribute("AdmissionIds",AdmissionIds);
        List<BatchMaster> batchList=batchMasterRepository.findByCourseId(courseIds);
        for(int i=0;i<batchList.size();i++)
        {
            System.out.println(batchList.get(i).getName());
        }
        System.out.println("Admission Ids " +AdmissionIds);
        List<AttendanceMaster> attendanceMasterList=attendanceRepository.findByAdmissionIds(AdmissionIds);

        for(int i=0;i<attendanceMasterList.size();i++)     //Find Student Percentage
        {
            if(attendanceMasterList.get(i).getStatus().equals("Present"))
            {
                System.out.println("Ho Rha Calculate");
                present++;
            }
            totalAttendance++;
        }
        double perc=0;
        if(present != 0)
            perc = (present * 100.0) / totalAttendance;

        System.out.println("Percentage "+ perc);
        String badgeClass, status;

        if (perc >= 75) {
            badgeClass = "badge bg-success";
            status = "Good";
        } else if (perc >= 50) {
            badgeClass = "badge bg-warning text-dark";
            status = "Average";
        } else {
            badgeClass = "badge bg-danger";
            status = "Poor";
        }

        model.addAttribute("badgeClass",badgeClass);
        model.addAttribute("status",status);
        model.addAttribute("percentage",perc);
        session.setAttribute("Batches",batchList);
        model.addAttribute("AllStudent",attendanceMasterList);
        model.addAttribute("Batches",batchList);

        System.out.println("Ended");
        return "StudentAttendance";
    }
    @GetMapping("/getallstudent")
    public String GetStudents(Model model,HttpSession session,@RequestParam("batchId") Long batchId,@RequestParam("courseId") Long courseId)
    {
        totalAttendance=0L;
        present=0L;
        List<BatchMaster> batchlist= (List<BatchMaster>) session.getAttribute("Batches");
        if(batchlist == null)
        {
            model.addAttribute("Error","Batch List Not Found");
            return "StudentAttendance";
        }

        List<AdmissionMaster> admission=admissionRepository.findByUserMasterAndCourseMaster(3L,courseId);
        List<String> admissionIds=new ArrayList<>();

        for(int i=0;i<admission.size();i++)
        {
            admissionIds.add(admission.get(i).getAdmissionId());
        }
        List<AttendanceMaster> attendanceMasterList=attendanceRepository.findByAdmissionIds(admissionIds);

        for(int i=0;i<attendanceMasterList.size();i++)       //Find Student Percentage
        {
            if(attendanceMasterList.get(i).getStatus().equals("Present"))
            {
                present++;
            }
            totalAttendance++;
        }
        double perc=0;
        if(present != 0)
            perc = (present * 100.0) / totalAttendance;

        System.out.println(perc);
        String badgeClass, status;

        if (perc >= 75) {
            badgeClass = "badge bg-success";
            status = "Good";
        } else if (perc >= 50) {
            badgeClass = "badge bg-warning text-dark";
            status = "Average";
        } else {
            badgeClass = "badge bg-danger";
            status = "Poor";
        }

        model.addAttribute("badgeClass",badgeClass);
        model.addAttribute("status",status);
        model.addAttribute("percentage",perc);

        return "StudentAttendance";
    }




//    <--------------------------------  Admin Part Start From Here     --------------------------------------->

    @GetMapping("/adminattendance")
    public String Admin(HttpSession session,Model model)
    {
        List<BatchMaster> AllBatch=batchMasterRepository.findAll();
        session.setAttribute("Batches",AllBatch);
        model.addAttribute("Batches",AllBatch);
        model.addAttribute("AllStudent",null);
        return "AdminAttendance";
    }


    @GetMapping("/getstudentsattendance/{batchId}")
    @ResponseBody
    public List<ShowAttendanceDTO> getStudentsAttendance(@PathVariable("batchId") Long batchId) {


        System.out.println("Batch Id"+batchId);

        // Find admissions directly by batch
        List<AdmissionMaster> admissions = admissionRepository.findByBatchId(batchId);

        List<String> admissionIds = admissions.stream()
                .map(AdmissionMaster::getAdmissionId)
                .toList();

        if (admissionIds.isEmpty()) {
            System.out.println("Empty List");
            return Collections.emptyList();
        }
        System.out.println("After Empty List");

        // admissionId → fullName
        Map<String, String> admissionNameMap = admissions.stream()
                .collect(Collectors.toMap(AdmissionMaster::getAdmissionId,
                        a -> a.getUserMaster().getFullName()));

        Long studentCount = (long) admissionIds.size();

        // total sessions (per student basis)
        Long totalAttendance = attendanceRepository.findByTotalAttendanceCount(admissionIds);
        totalAttendance = totalAttendance / studentCount;

        // per student present count
        List<Object[]> results = attendanceRepository.findPresentCountPerAdmission(admissionIds);

        List<ShowAttendanceDTO> allStudents = new ArrayList<>();

        for (Object[] row : results) {
            String admissionId = (String) row[0];
            Long presentCount = (Long) row[1];
            Long absentCount = totalAttendance - presentCount;

            double perc = (presentCount * 100.0) / totalAttendance;

            String badgeClass, status;
            if (perc >= 75) {
                badgeClass = "badge bg-success";
                status = "Good";
            } else if (perc >= 50) {
                badgeClass = "badge bg-warning text-dark";
                status = "Average";
            } else {
                badgeClass = "badge bg-danger";
                status = "Poor";
            }

            allStudents.add(new ShowAttendanceDTO(
                    admissionId,
                    admissionNameMap.get(admissionId),
                    presentCount,
                    absentCount,
                    perc,
                    badgeClass,
                    status,
                    totalAttendance
            ));
        }

        System.out.println("Ended");
        System.out.println(allStudents.size());
//
        return allStudents;
    }


    @GetMapping("/getstudentsattendancefulldetails/{batchId}")
    @ResponseBody
    public Map<String,Object> getStudentsAttendanceFullDetails(@PathVariable("batchId") Long batchId) {
        List<AdmissionMaster> allAdmissions = admissionRepository.findByBatchId(batchId);

        List<StudentDTO> students = allAdmissions.stream()
                .map(ad -> {
                    StudentDTO dto = new StudentDTO();
                    dto.setAdmissionId(ad.getAdmissionId());
                    dto.setFullName(ad.getUserMaster().getFullName()); // ya jo field hai
                    return dto;
                }).toList();

        List<AttendanceDTO> attendance = attendanceRepository.findByAdmissionIds(
                allAdmissions.stream().map(AdmissionMaster::getAdmissionId).toList()
        ).stream().map(att->new AttendanceDTO(att.getAdmission().getAdmissionId(),att.getStatus(),att.getAttendanceDate().toString())).toList();

        System.out.println("Total Student size "+students.size());
        System.out.println("Attendance Size "+attendance.size());
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("students", students);
    objectMap.put("attendance", attendance);
    return objectMap;
}


//    @GetMapping("/getstudentsattendancefulldetails/{batchId}")
//    @ResponseBody
//    public StudentAttendanceResponse getStudentsAttendanceFullDetails(@PathVariable("batchId") Long batchId) {
//        System.out.println("Hao bhaiya aa gay apan andar");
//
//        List<AdmissionMaster> allAdmissions = admissionRepository.findByBatchId(batchId);
//        List<String> admissionIds = new ArrayList<>();
//        for (AdmissionMaster ad : allAdmissions) {
//            admissionIds.add(ad.getAdmissionId());
//        }
//
//        List<AttendanceMaster> allAttendanceOfStudents = attendanceRepository.findByAdmissionIds(admissionIds);
//
//        StudentAttendanceResponse response = new StudentAttendanceResponse();
//        response.setStudents(allAdmissions);
//        response.setAttendance(allAttendanceOfStudents);
//
//        return response;
//    }


//    @GetMapping("/getstudentsattendancefulldetails/{batchId}")
//    @ResponseBody
//    public Map<Integer,Object> getStudentsAttendanceFullDetails(@PathVariable("batchId")Long batchId){
//        System.out.println("Hao bhaiya aa gay apan andar");
//        List<AdmissionMaster>allAdmissions=admissionRepository.findByBatchId(batchId);
//        List<String> admissionIds=new ArrayList<>();
//        for(AdmissionMaster ad : allAdmissions){
//            admissionIds.add(ad.getAdmissionId());
//        }
//        List<AttendanceMaster> allAttendanceOfStudents=attendanceRepository.findByAdmissionIds(admissionIds);
//        Map<Integer,Object>objectMap=new HashMap<>();
//        objectMap.put(1,allAdmissions);
//        objectMap.put(2,allAttendanceOfStudents);
////        objectMap.get(1)
//        return objectMap;
////        objectMap
//
//    }

@GetMapping("/showallstudent")
public String showAllStudent(HttpSession session,@RequestParam("courseId") Long courseId, Model model) {
    // load students by courseId
    List<BatchMaster> batchlist= (List<BatchMaster>) session.getAttribute("Batches");
    System.out.println(courseId);

    List<AdmissionMaster> ids=admissionRepository.findByCourse(courseId);
    List<String> AdmissionIds=new ArrayList<>();
    Long count=0L;
    for(int i=0;i<ids.size();i++)
    {
        AdmissionIds.add(ids.get(i).getAdmissionId());
        System.out.println(ids.get(i).getUserMaster().getFullName());
        System.out.println(ids.get(i).getAdmissionId());
        count++;
    }
    System.out.println(AdmissionIds);

        //Distinct Ids of Attendance Master ;
        List<AttendanceMaster> attendanceMasterList=attendanceRepository.findLatestByAdmissionIds(AdmissionIds);
        for(int i=0;i<attendanceMasterList.size();i++)
        {
            System.out.println(attendanceMasterList.get(i).getStatus());
        }
        //Find All Date Attendance for modals
        List<AttendanceMaster> AllattendanceOfStudents=attendanceRepository.findByAdmissionIds(AdmissionIds);

        //Count Total Attendance
        Long TotalAttendance= attendanceRepository.findByTotalAttendanceCount(AdmissionIds);
        System.out.println(TotalAttendance);
        TotalAttendance/=count;

        Long presenties=attendanceRepository.findByCount(AdmissionIds);
        System.out.println("Presenties "+presenties);

        List<Object[]> results = attendanceRepository.findPresentCountPerAdmission(AdmissionIds);
        List<Map<String,Object>> AllStudent=new ArrayList<>();

        for (Object[] row : results) {
            String admissionId = (String) row[0];
            Long presentCount = (Long) row[1];
            Long absentCount = TotalAttendance-presentCount;
            Map<String,Object> map=new HashMap<>();

            String fullName="";
            for(AdmissionMaster ads : ids)
            {
                if(ads.getAdmissionId().equals(admissionId))
                {
                    fullName=ads.getUserMaster().getFullName();
                    System.out.println("Full Name Is "+fullName);
                    break;
                }
            }
            double perc = (presentCount * 100.0) / TotalAttendance;
            String badgeClass, status;

            if (perc >= 75) {
                badgeClass = "badge bg-success";
                status = "Good";
            } else if (perc >= 50) {
                badgeClass = "badge bg-warning text-dark";
                status = "Average";
            } else {
                badgeClass = "badge bg-danger";
                status = "Poor";
            }

            System.out.println("Student " + admissionId + " is present: " + presentCount +"Absent is "+absentCount);
            map.put("admissionId",admissionId);
            map.put("presentCount",presentCount);
            map.put("absentCount",absentCount);
            map.put("badgeClass",badgeClass);
            map.put("fullName",fullName);
            map.put("status",status);
            map.put("percentage",perc);
            AllStudent.add(map);
        }
        model.addAttribute("TotalAttendance",TotalAttendance);
        model.addAttribute("AllStudent",AllStudent);
        model.addAttribute("AllDatesAttendance",AllattendanceOfStudents);
        model.addAttribute("Batches",batchlist);
        model.addAttribute("SelectedCourseId",courseId);
        model.addAttribute("openModal",true);
        return "AdminAttendance";
    }

}
