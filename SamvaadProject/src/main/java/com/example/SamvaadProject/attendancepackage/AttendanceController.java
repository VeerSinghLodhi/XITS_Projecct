package com.example.SamvaadProject.attendancepackage;

import com.example.SamvaadProject.admissionpackage.AdmissionDTO;
import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.attendance_view.AttendanceView;
import com.example.SamvaadProject.attendance_view.ShowAttendanceDTO;
import com.example.SamvaadProject.attendance_view.attendanceview_repo;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import com.example.SamvaadProject.coursepackage.CourseRepository;
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

//    @GetMapping("/facultyattendance")
//    public  String Attendance(Model model, HttpSession session)
//    {
//        List<BatchMaster> batchlist=batchMasterRepository.findByFacultyId(2);
//        model.addAttribute("Batches",batchlist);
//        session.setAttribute("Batches",batchlist);
//        LocalDate date=LocalDate.now();
//        model.addAttribute("selectedDate",date);
//        return "attendanceForm";
//    }

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

//    @GetMapping("/coursePage")
//    public String getId(@RequestParam("batchId") Long batchId,Model model,HttpSession session)
//    {
//        List<BatchMaster> batchList= (List<BatchMaster>) session.getAttribute("Batches");
//
//        session.setAttribute("batchid",batchId);
//        session.setAttribute("Batches",batchList);
//        List<AttendanceView> attendanceView=attendanceviewRepo.findByBatchId(batchId.longValue());
//        session.setAttribute("Studentname",attendanceView);
//
//        LocalDate date=LocalDate.now();
//        model.addAttribute("selectedDate",date);
//        model.addAttribute("Studentname",attendanceView);
//        model.addAttribute("selectedBatchId",batchId);
//        model.addAttribute("Batches",batchList);
//        System.out.println("Select Wali "+batchId);
//        return "attendanceForm";
//    }

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
//            System.out.println("Admission Id "+admissionidList.get(i)+" isPresent "+isPresent);
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





//    <--------------------------------  Admin Part Start From Here     --------------------------------------->



        @GetMapping("/getstudentsattendance/{batchId}")
        @ResponseBody
        public List<ShowAttendanceDTO> getStudentsAttendance(@PathVariable("batchId") Long batchId) {

            System.out.println("Batch Id: " + batchId);

            List<AdmissionMaster> admissions = admissionRepository.findByBatchId(batchId);
            if (admissions.isEmpty()) {
                System.out.println("No admissions found for this batch");
                return Collections.emptyList();
            }

            // admissionId → fullName
            Map<String, String> admissionNameMap = admissions.stream()
                    .collect(Collectors.toMap(AdmissionMaster::getAdmissionId,
                            a -> a.getUserMaster().getFullName()));

            // All admissionIds
            List<String> admissionIds = new ArrayList<>(admissionNameMap.keySet());

            // 🟢 Get distinct total sessions in this batch (count distinct dates)
            Long totalSessions = attendanceRepository.countDistinctSessionsByBatchId(batchId);
            if (totalSessions == null || totalSessions == 0) {
                totalSessions = 0L;
            }

            // 🟢 Get per student present count
            List<Object[]> results = attendanceRepository.findPresentCountPerAdmission(admissionIds);

            List<ShowAttendanceDTO> allStudents = new ArrayList<>();

            for (AdmissionMaster admission : admissions) {
                String admissionId = admission.getAdmissionId();
                String fullName = admissionNameMap.get(admissionId);

                Long presentCount = results.stream()
                        .filter(r -> admissionId.equals(r[0]))
                        .map(r -> (Long) r[1])
                        .findFirst()
                        .orElse(0L);

                Long absentCount = (totalSessions > 0) ? (totalSessions - presentCount) : 0L;

                double perc = (totalSessions > 0) ? (presentCount * 100.0 / totalSessions) : 0.0;

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
                        fullName,
                        presentCount,
                        absentCount,
                        perc,
                        badgeClass,
                        status,
                        totalSessions
                ));
            }

            System.out.println("Total students processed: " + allStudents.size());
            return allStudents;
        }

    @GetMapping("/getstudentsattendancefulldetails/{batchId}")
    @ResponseBody
    public Map<String, Object> getStudentsAttendanceFullDetails(@PathVariable("batchId") Long batchId) {
        List<AdmissionMaster> allAdmissions = admissionRepository.findByBatchId(batchId);

        List<StudentDTO> students = allAdmissions.stream()
                .map(ad -> new StudentDTO(ad.getAdmissionId(), ad.getUserMaster().getFullName()))
                .toList();

        List<AttendanceDTO> attendance = attendanceRepository.findByAdmissionIds(
                        allAdmissions.stream().map(AdmissionMaster::getAdmissionId).toList()
                ).stream()
                .map(att -> new AttendanceDTO(
                        att.getAdmission().getAdmissionId(),
                        att.getStatus(),
                        att.getAttendanceDate().toString()
                ))
                .toList();

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("students", students);
        objectMap.put("attendance", attendance);
        return objectMap;
    }



//            @GetMapping("/getstudentsattendancefulldetails/{batchId}")
//            @ResponseBody
//            public Map<String,Object> getStudentsAttendanceFullDetails(@PathVariable("batchId") Long batchId) {
//                List<AdmissionMaster> allAdmissions = admissionRepository.findByBatchId(batchId);
//
//                List<StudentDTO> students = allAdmissions.stream()
//                        .map(ad -> {
//                            StudentDTO dto = new StudentDTO();
//                            dto.setAdmissionId(ad.getAdmissionId());
//                            dto.setFullName(ad.getUserMaster().getFullName()); // ya jo field hai
//                            return dto;
//                        }).toList();
//
//                List<AttendanceDTO> attendance = attendanceRepository.findByAdmissionIds(
//                        allAdmissions.stream().map(AdmissionMaster::getAdmissionId).toList()
//                ).stream().map(att->new AttendanceDTO(att.getAdmission().getAdmissionId(),
//                        att.getStatus(),
//                        att.getAttendanceDate().toString()))
//                        .toList();
//
//                System.out.println("Total Student size "+students.size());
//                System.out.println("Attendance Size "+attendance.size());
//                Map<String,Object> objectMap = new HashMap<>();
//                objectMap.put("students", students);
//            objectMap.put("attendance", attendance);
//            return objectMap;
//        }
//


}
