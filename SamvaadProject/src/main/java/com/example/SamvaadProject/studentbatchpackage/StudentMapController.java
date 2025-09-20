package com.example.SamvaadProject.studentbatchpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/studentbatch/students/{batchId}")
    @ResponseBody
    public List<String> getStudentAccordingToBatch(@PathVariable("batchId")Long batchId){
        return studentBatchRepository.findByBatch_BatchId(batchId)
                .stream()
                .map(map -> map.getAdmission().getAdmissionId())
                .toList();
    }


}
