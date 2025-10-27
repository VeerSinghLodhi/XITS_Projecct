package com.example.SamvaadProject.assignmentpackage;

import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import com.example.SamvaadProject.usermasterpackage.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Controller
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private BatchMasterRepository batchMasterRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/done")
    public String uploadAssignment(@RequestParam("batchId") Long batchId,
                                   @RequestParam("file") MultipartFile file,
                                   Model model,
                                   @RequestParam("title")String title,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            BatchMaster batch = batchMasterRepository.findById(batchId)
                    .orElseThrow(() -> new RuntimeException("Batch not found"));

            AssignmentMaster assignment = new AssignmentMaster();
            assignment.setTitle(title);
            assignment.setBatch(batch);
            assignment.setPdfs(file.getBytes());
            assignment.setProfessor(batch.getFaculty());
            assignment.setPdfDate(LocalDate.now());
            assignment.setPdfName(file.getOriginalFilename());

            assignmentRepository.save(assignment);

            redirectAttributes.addAttribute("newAssignmentAdded",true);

            return "redirect:/faculty/dashboard#assignments";
        } catch (Exception e) {
            model.addAttribute("error", "Error saving assignment: " + e.getMessage());
        }
        model.addAttribute("error","Session Expired!");
        return "login";
    }


    @PostMapping("/delete_assignment/{assignmentId}")
    public String deleteAssignment(@PathVariable("assignmentId") Long assignmentId,
                                   RedirectAttributes redirectAttributes,HttpSession session) {
        System.out.println("Assignment Id: " + assignmentId);
        assignmentRepository.getAssignmentDelete(assignmentId);
        redirectAttributes.addAttribute("assignmentDeleted", true);
        return "redirect:/faculty/dashboard#assignments#deleteassignment";
    }


    private void populateModel(Model model) {
        model.addAttribute("batches", batchMasterRepository.findAll());
        model.addAttribute("assignment", new AssignmentMaster());
        model.addAttribute("user", userRepository.findAll());

        List<AssignmentMaster> allAssignments = assignmentRepository.findAll();

        List<AssignmentGroup> groupedAssignments = allAssignments.stream()
                .collect(Collectors.groupingBy(a -> a.getBatch().getName()))
                .entrySet()
                .stream()
                .map(e -> new AssignmentGroup(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        model.addAttribute("groupedAssignments", groupedAssignments);
    }

    @PostMapping("/updateassignment/{id}")
    public String updatePdf(@PathVariable("id") Long id,
                                            @RequestParam("file") MultipartFile file,
                                            HttpSession session,
                                            RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Entered Inside the Update Assignment Method");
            Optional<AssignmentMaster> assignmentOpt = assignmentRepository.findById(id);
            if (assignmentOpt.isEmpty()) {
                return "redirect:/faculty/dashboard";
            }

            AssignmentMaster assignment = assignmentOpt.get();
            assignment.setPdfName(file.getOriginalFilename());
            assignment.setPdfs(file.getBytes());
            assignment.setPdfDate(LocalDate.now()); // update date
            System.out.println("Update Assignment OKK");
            assignmentRepository.save(assignment);

            redirectAttributes.addAttribute("assignmentUpdated",true);
                System.out.println("Assignment Updated");
            return "redirect:/faculty/dashboard#assignments#updateassignment";
        } catch (Exception e) {
            e.printStackTrace();
            return "login";
        }
    }


    // Get Assignment By batch id
@GetMapping("/allassignmetbybatchid/{batchId}")
    @ResponseBody
    public List<AssignmentDTO>getAllAssingmentByBatchId(@PathVariable("batchId")Long batchId){
        return assignmentRepository.getAllAssignmentByBatchId(batchId)
                .stream()
                .map(dto -> new AssignmentDTO(dto.getAssignmentId(),dto.getTitle(),dto.getBatch().getName(), dto.getPdfName(),dto.getPdfDate().toString()))
                .toList();
    }



}
