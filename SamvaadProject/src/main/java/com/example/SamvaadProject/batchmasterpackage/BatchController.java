package com.example.SamvaadProject.batchmasterpackage;

import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.coursepackage.CourseRepository;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import com.example.SamvaadProject.usermasterpackage.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BatchController {
    @Autowired
    BatchMasterRepository batchMasterRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;





    @PostMapping("/assign")
    public String addBatch(@ModelAttribute("newbatch")BatchMaster newbatch,
                           @RequestParam("courseId") Long courseId,
                           @RequestParam("userId") Long facultyId,
                           Model model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes){

    newbatch.setEndDate(null);
    newbatch.setCourse(courseRepository.findById(courseId).orElse(null));
    newbatch.setFaculty(userRepository.findById(facultyId).orElse(null));
    newbatch.setStatus("ACTIVE");
    batchMasterRepository.save(newbatch);
    redirectAttributes.addAttribute("newBatchAdded",true);

    return "redirect:/admin/dashboard#batch-part";
    }
    
    @GetMapping("/batch/batchdetail/{id}")
    @ResponseBody
    public BatchDTO getBatchDetailInJson(@PathVariable("id")Long id){

        BatchMaster batchMaster=batchMasterRepository.findById(id).orElse(null);
        BatchDTO batchDTO =new BatchDTO();

        batchDTO.setBatchId(batchMaster.getBatchId());
        batchDTO.setCourseId(batchMaster.getCourse().getCourseId());
        batchDTO.setFacultyId(batchMaster.getFaculty().getUserId());
        batchDTO.setMode(batchMaster.getMode());
        batchDTO.setStatus(batchMaster.getStatus());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        batchDTO.setStartDate(batchMaster.getStartDate().format(formatter));

        return batchDTO;
    }

    @PostMapping("/batch/update")
    public String getUpdateBatch(@RequestParam("batchId")Long batchId,
                                 @RequestParam("courseId")Long courseId,
                                 @RequestParam("userId")Long userId,
                                 @RequestParam("mode")String mode,
                                 @RequestParam("startDate")LocalDateTime startDate,
                                 @RequestParam("status")String status,
                                 RedirectAttributes redirectAttributes){

        BatchMaster updateBatchMaster=batchMasterRepository.findById(batchId).orElse(null);

        updateBatchMaster.setCourse(courseRepository.findById(courseId).orElse(null));
        updateBatchMaster.setFaculty(userRepository.findById(userId).orElse(null));
        updateBatchMaster.setMode(mode);
        updateBatchMaster.setStatus(status);
        updateBatchMaster.setStartDate(startDate);

        batchMasterRepository.save(updateBatchMaster);

        redirectAttributes.addAttribute("batchUpdated",true);

        return "redirect:/admin/dashboard#batch-part#update_batch";
    }


    // Faculties Batches

    @GetMapping("/batch/faculty/{facultyId}/batches")
    @ResponseBody
    public List<BatchDTO>getFacultyBatches(@PathVariable("facultyId")Long facultyId){

        return batchMasterRepository.findByFacultyId(facultyId)
                .stream()
                .map(faculty ->new BatchDTO(faculty.getBatchId(),
                        faculty.getStartDate().toString(),
                        faculty.getStatus(),
                        faculty.getMode(),
                        faculty.getCourse().getCourseId(),
                        faculty.getFaculty().getUserId(),
                        faculty.getCourse().getCourseName(),faculty.getName()))
                .toList();
    }


}

