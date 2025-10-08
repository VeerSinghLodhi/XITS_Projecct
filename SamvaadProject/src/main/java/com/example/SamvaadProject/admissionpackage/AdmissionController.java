package com.example.SamvaadProject.admissionpackage;

import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.coursepackage.CourseRepository;
import com.example.SamvaadProject.studentbatchpackage.StudentBatchMap;
import com.example.SamvaadProject.studentbatchpackage.StudentBatchRepository;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import com.example.SamvaadProject.usermasterpackage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AdmissionController {

    @Autowired
    AdmissionRepository admissionRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentBatchRepository studentBatchRepository;

    @PostMapping("/admin/admission")
    public String addStudent(@ModelAttribute("newadmission")AdmissionMaster newAdmission,
                             @RequestParam("user_id")Long userId,
                             @RequestParam("course_id")Long courseId,
                             RedirectAttributes redirectAttributes){

        UserMaster userMaster=userRepository.findById(userId).orElse(null);
        CourseMaster courseMaster=courseRepository.findById(courseId).orElse(null);

        newAdmission.setAdmissionId(getAdmissionPrimaryKey());
        newAdmission.setUserMaster(userMaster);
        newAdmission.setCourse(courseMaster);
        newAdmission.setJoinDate(new Date());

        admissionRepository.save(newAdmission);
        redirectAttributes.addAttribute("newAdmissionAdded",true);

        return "redirect:/admin/dashboard#admission";
    }

    @GetMapping("/admissiondetail/{id}")
    @ResponseBody
    public AdmissionDTO  getAdmissionDetailById(@PathVariable("id")String admissionId){

        AdmissionMaster admission= admissionRepository.findById(admissionId).orElse(null);
        if (admission == null) return null;

        AdmissionDTO dto = new AdmissionDTO();
        dto.setAdmissionId(admission.getAdmissionId());
        dto.setFees(admission.getFees());
        dto.setDiscount(admission.getDiscount());
        dto.setCourseId(admission.getCourse().getCourseId());
        dto.setCourseName(admission.getCourse().getCourseName());
        dto.setBalance(admission.getBalance());

        return dto;
    }

    // Update Admission Records
    @PostMapping("/admin/updateadmission")
    public String getUpdateAdmission(@RequestParam("admissionID")String admissionID,
                                     @RequestParam("course_id1")Long courseId,
                                     @RequestParam("fees1")Double fees,
                                     @RequestParam("discount1")Double discount,
                                     RedirectAttributes redirectAttributes){
        System.out.println("Admission Id "+admissionID);
        AdmissionMaster admissionMaster=admissionRepository.findById(admissionID).orElse(null);
        admissionMaster.setCourse(courseRepository.findById(courseId).orElse(null));
        admissionMaster.setFees(fees);
        admissionMaster.setDiscount(discount);

        admissionRepository.save(admissionMaster);  // Record Updated.
        redirectAttributes.addAttribute("admissionUpdated",true);
        return "redirect:/admin/dashboard";
    }


//    @GetMapping("/admissions/{userId}")
//    @ResponseBody
//    public List<AdmissionDTO> getAllAdmission(@PathVariable("userId")Long userId){
//        return admissionRepository.findByUserMaster_UserId(userId)
//                .stream()
//                .map(ad -> new AdmissionDTO(
//                        ad.getAdmissionId(),
//                        ad.getCourse().getCourseName(),
//                        studentBatchRepository.findByAdmission_AdmissionId(ad.getAdmissionId()).getBatch().getName().toString(),
//                        ad.getJoinDate().toString()
//                ))
//                .toList();
//    }
        @GetMapping("/admissions/{userId}")
        @ResponseBody
        public List<AdmissionDTO> getAllAdmission(@PathVariable("userId") Long userId) {
            return admissionRepository.findByUserMaster_UserId(userId)
                    .stream()
                    .map(ad -> {
                        var studentBatch = studentBatchRepository.findByAdmission_AdmissionId(ad.getAdmissionId());
                        Double balance=admissionRepository.getBalanceByStudent(userId);
                        System.out.println("Balance is "+balance);
                        String batchName = (studentBatch != null && studentBatch.getBatch() != null)
                                ? studentBatch.getBatch().getName()
                                : "Not Assigned";

                        return new AdmissionDTO(
                                ad.getAdmissionId(),
                                ad.getCourse().getCourseName(),
                                batchName,
                                ad.getJoinDate().toString()
                        );
                    })
                    .toList();
        }
//@GetMapping("/admissions/{userId}")
//@ResponseBody
//public List<AdmissionDTO> getAllAdmission(@PathVariable("userId") Long userId) {
//    return admissionRepository.findByUserMaster_UserId(userId)
//            .stream()
//            .map(ad -> {
//                // fetch all student-batch mappings for this admission
//                List<StudentBatchMap> studentBatches = studentBatchRepository.findByAdmission_AdmissionId(ad.getAdmissionId());
//
//                String batchName = studentBatches.isEmpty()
//                        ? "Not Assigned"
//                        : studentBatches.stream()
//                        .map(sb -> sb.getBatch().getName())
//                        .collect(Collectors.joining(", ")); // join with commas
//
//                return new AdmissionDTO(
//                        ad.getAdmissionId(),
//                        ad.getCourse().getCourseName(),
//                        batchName,
//                        ad.getJoinDate().toString()
//                );
//            })
//            .toList();
//}



    public String getAdmissionPrimaryKey(){

        Long maxAdmissionNumber=admissionRepository.getCount()+1;

        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int mm=calendar.get(Calendar.MONTH)+1;
        String format,counter;
        if(maxAdmissionNumber<10){
            counter="0"+maxAdmissionNumber;
        }else{
            counter=""+maxAdmissionNumber;
        }
        if(mm<10){
            format=year+"0"+mm+""+counter;
        }else{
            format=year+""+mm+""+counter;
        }
        return format;
    }
}
