package com.example.SamvaadProject.usermasterpackage;

import com.example.SamvaadProject.assignmentpackage.AssignmentGroup;
import com.example.SamvaadProject.assignmentpackage.AssignmentMaster;
import com.example.SamvaadProject.assignmentpackage.AssignmentRepository;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Login {

    @Autowired
    private BatchMasterRepository batchMasterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @GetMapping("/login2")
    public String showLoginPage() {
        return "login2";
    }

    @PostMapping("/login2")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {
        UserMaster user = userRepository.getByUsernameAndPassword(username, password);

        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login2";
        }

        session.setAttribute("user", user);

        if (user.getRole() == UserMaster.Role.ADMIN) {
            populateModel(model);
            return "Add_Assignment";
        } else if (user.getRole() == UserMaster.Role.STUDENT) {
            return "redirect:/studentAssignments/list";
        } else {
            model.addAttribute("error", "Role not recognized");
            return "login2";
        }
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

        LocalDate weekAgo = LocalDate.now().minusDays(7);
        List<AssignmentMaster> oldAssignments = assignmentRepository.findByPdfDateBefore(weekAgo);

        if (!oldAssignments.isEmpty()) {
            model.addAttribute("warningAssignments", oldAssignments);
            model.addAttribute("warningMessage", "Some assignments are older than 7 days!");
        }
    }
}