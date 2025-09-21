package com.example.SamvaadProject.studentbatchpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionDTO;
import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class StudentMapController {

    @Autowired
    StudentBatchRepository studentBatchRepository;

    @Autowired
    BatchMasterRepository batchMasterRepository;

    @Autowired
    AdmissionRepository admissionRepository;

    @PostMapping("/studentbatch/save")
    public String saveStudentBatch(
            @RequestParam("batchId") Long batchId,
            @RequestParam("admissionIds") List<String> admissionIds,
            RedirectAttributes redirectAttributes) {

        // Loop through student IDs and save mapping
        for (String admissionId : admissionIds) {
            StudentBatchMap sbm = new StudentBatchMap();

            BatchMaster batch = batchMasterRepository.findById(batchId).orElseThrow();
            AdmissionMaster admission = admissionRepository.findById(admissionId).orElseThrow();

            sbm.setBatch(batch);
            sbm.setAdmission(admission);

            studentBatchRepository.save(sbm);
        }
        redirectAttributes.addAttribute("studentsMapped",true);


        return "redirect:/admin/dashboard";
    }

    @GetMapping("/students/{batchId}")//students/4
    @ResponseBody
    public List<Map<String, Object>> getStudentsByBatch(@PathVariable("batchId") Long batchId) {
        List<StudentBatchMap> mappings = studentBatchRepository.findByBatch_BatchId(batchId);//4

        // Return minimal student info
        List<Map<String, Object>> students = new ArrayList<>();
        for (StudentBatchMap map : mappings) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("admissionId", map.getAdmission().getAdmissionId());
            obj.put("studentName", map.getAdmission().getUserMaster().getFullName());
            students.add(obj);
        }
        return students;
    }


//    @GetMapping("/studentbatch/students/{batchId}")
//    @ResponseBody
//    public Map<String, Object> getStudentAccordingToBatch(@PathVariable("batchId") Long batchId) {
//        // Step 1: Get mappings for this batch
//        List<StudentBatchMap> mappings = studentBatchRepository.findByBatch_BatchId(batchId);
//        System.out.println("Student Batch Map Size"+mappings.size());
//        System.out.println("==============Before Empty checking=================");
////        if (mappings.isEmpty()) {
////            return Map.of("admissions", Collections.emptyList(),
////                    "assignedIds", Collections.emptyList()
////            );
////        }
//
//        // Step 2: Get courseId from batch
//        Long courseId = mappings.get(0).getBatch().getCourse().getCourseId();
//
//        // Step 3: Get all admissions of this course
//        List<AdmissionDTO> allAdmissions = admissionRepository.findByCourse_CourseId(courseId)
//                .stream()
//                .map(ad -> new AdmissionDTO(ad.getAdmissionId(), ad.getUserMaster().getFullName()))
//                .toList();
//
//        // Step 4: Collect IDs of admissions already assigned
//        Set<String> assignedAdmissionIds = mappings.stream()
//                .map(m -> m.getAdmission().getAdmissionId())
//                .collect(Collectors.toSet());
//
//        System.out.println("==============OK=================");
//        // Step 5: Return both lists
//        return Map.of(
//                "admissions", allAdmissions,
//                "assignedIds", assignedAdmissionIds
//        );
//    }






    @GetMapping("/studentbatch/students/{batchId}")
    @ResponseBody
    public List<String> getStudentAccordingToBatch(@PathVariable("batchId")Long batchId){
        return studentBatchRepository.findByBatch_BatchId(batchId)
                .stream()
                .map(map -> map.getAdmission().getAdmissionId())
                .toList();
    }
//        // Step 1: Get all StudentBatchMap entries for this batch
//        List<StudentBatchMap> mappings = studentBatchRepository.findByBatch_BatchId(batchId);
//
//        // Step 2: Create a list to store admission IDs
//        List<String> admissionIds = new ArrayList<>();
//
//        // Step 3: Loop through each mapping and extract admissionId
//        for (StudentBatchMap map : mappings) {
//            String admissionId = map.getAdmission().getAdmissionId();
//            admissionIds.add(admissionId);
//        }
//        List<String>finalAdmissions=new ArrayList<>();
//        for(String aid : admissionIds ) {
//            if(mappings.get(0).getBatch().getCourse().getCourseId()==
//                    admissionRepository.findById(aid).orElse(null).getCourse().getCourseId()){
//                finalAdmissions.add(aid);
//            }
//        }
//        System.out.println(admissionIds);
//        System.out.println(finalAdmissions);
//
//        // Step 4: Return the list of admission IDs
//        return finalAdmissions;



}
