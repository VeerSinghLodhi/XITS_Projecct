package com.example.SamvaadProject.assignmentpackage;

import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import com.example.SamvaadProject.usermasterpackage.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private BatchMasterRepository batchMasterRepository;

    @Autowired
    private UserRepository userRepository;

//    private void populateModel(Model model) {
//        model.addAttribute("batches", batchMasterRepository.findAll());
//        model.addAttribute("assignment", new AssignmentMaster());
//        model.addAttribute("user", userRepository.findAll());
//
//        List<AssignmentMaster> allAssignments = assignmentRepository.findAll();
//    }

    @GetMapping("/add_assignment")
    public String showAddAssignmentPage(Model model, HttpSession session) {
        UserMaster user = (UserMaster) session.getAttribute("user");
        if (user == null) return "login";

        if (user.getRole() == UserMaster.Role.ADMIN) {
            populateModel(model);
            return "Add_Assignment";
        } else {
            model.addAttribute("error", "You are not authorized to access this page.");
            return "error";
        }
    }
    @PostMapping("/done")
    public String uploadAssignment(@RequestParam("batchId") Long batchId,
                                   @RequestParam("file") MultipartFile file,
                                   Model model,
                                   HttpSession session) {
        UserMaster user = (UserMaster) session.getAttribute("user");
        if (user == null || user.getRole() != UserMaster.Role.ADMIN) {
            model.addAttribute("error", "Unauthorized access.");
            return "error";
        }
        try {
            BatchMaster batch = batchMasterRepository.findById(batchId)
                    .orElseThrow(() -> new RuntimeException("Batch not found"));

            AssignmentMaster assignment = new AssignmentMaster();
            assignment.setBatch(batch);
            assignment.setPdfs(file.getBytes());
            assignment.setProfessor(user);
            assignment.setPdfDate(LocalDate.now());
            assignment.setPdfName(file.getOriginalFilename()); // save PDF name

            assignmentRepository.save(assignment);
            model.addAttribute("success", "Assignment uploaded successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error saving assignment: " + e.getMessage());
        }
        populateModel(model);
        return "Add_Assignment";
    }

    public void populateModel(Model model) {
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
    @PostMapping("/delete_assignment")
    public String deleteAssignment(@RequestParam("assignmentId") Long assignmentId,
                                   Model model,
                                   HttpSession session) {
        UserMaster user = (UserMaster) session.getAttribute("user");
        if (user == null || user.getRole() != UserMaster.Role.ADMIN) {
            model.addAttribute("error", "Unauthorized access.");
            return "error";
        }
        assignmentRepository.deleteById(assignmentId);
        model.addAttribute("success", "Assignment deleted successfully!");
        populateModel(model);
        return "Add_Assignment";
    }
}