package com.example.SamvaadProject.assignmentpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/studentAssignments")
public class StudentAssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private SubmitRepository submitAssignmentRepository;

    @Autowired
    private BatchMasterRepository batchMasterRepository;

    // List assignments
    @GetMapping("/list")
    public String listAssignments(HttpSession session, Model model) {
        UserMaster student = (UserMaster) session.getAttribute("user");
        if (student == null || student.getRole() != UserMaster.Role.STUDENT) {
            model.addAttribute("error", "Unauthorized access");
            return "error";
        }

        // Student batches
        List<BatchMaster> batches = batchMasterRepository.findBatchesByStudent(student.getUserId());
        List<Long> batchIds = batches.stream().map(BatchMaster::getBatchId).collect(Collectors.toList());

        // Assignments in batches
        List<AssignmentMaster> assignments = batchIds.isEmpty()
                ? Collections.emptyList()
                : assignmentRepository.findByBatch_BatchIdIn(batchIds);

        // Submitted assignments
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepository
                .findByAdmission_UserMaster_UserId(student.getUserId());

        List<Long> submittedAssignmentIds = submittedAssignments.stream()
                .map(sa -> sa.getAssignment().getAssignmentId())
                .collect(Collectors.toList());

        // Map to check delete eligibility (within 24 hours)
        Map<Long, Boolean> deletableMap = submittedAssignments.stream()
                .collect(Collectors.toMap(
                        sa -> sa.getAssignment().getAssignmentId(),
                        sa -> sa.getSubmittedAt() != null &&
                                (System.currentTimeMillis() - sa.getSubmittedAt().getTime()) < 86400000
                ));

        model.addAttribute("assignments", assignments);
        model.addAttribute("submittedAssignmentIds", submittedAssignmentIds);
        model.addAttribute("deletableMap", deletableMap);

        return "Student_assignment";
    }

    // Download assignment PDF
    @GetMapping("/pdfs/{id}")
    public ResponseEntity<byte[]> getPdf(@PathVariable("id") Long id) {
        Optional<AssignmentMaster> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isEmpty() || assignmentOpt.get().getPdfs() == null) {
            return ResponseEntity.notFound().build();
        }

        AssignmentMaster assignment = assignmentOpt.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + assignment.getPdfName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(assignment.getPdfs());
    }

    // Submit assignment
    @PostMapping("/submit")
    public String submitAssignment(@RequestParam("assignmentId") Long assignmentId,
                                   @RequestParam("file") MultipartFile file,
                                   HttpSession session) {
        UserMaster student = (UserMaster) session.getAttribute("user");
        if (student == null || student.getRole() != UserMaster.Role.STUDENT) {
            return "error";
        }

        AssignmentMaster assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        AdmissionMaster admission = student.getAdmissions().isEmpty() ? null : student.getAdmissions().get(0);
        if (admission == null) return "error";

        try {
            SubmitAssignment submission = submitAssignmentRepository
                    .findByAssignmentAndAdmission(assignment, admission)
                    .orElseGet(() -> {
                        SubmitAssignment sa = new SubmitAssignment();
                        sa.setAssignment(assignment);
                        sa.setAdmission(admission);
                        sa.setProfessor(assignment.getProfessor());
                        return sa;
                    });

            if (submission.getSubmittedAt() != null) {
                long diffMillis = System.currentTimeMillis() - submission.getSubmittedAt().getTime();
                if (diffMillis >= 86400000) return "redirect:/studentAssignments/list?error=late";
            }

            submission.setPdf(file.getBytes());
            submission.setSubmittedAt(new Date());
            submitAssignmentRepository.save(submission);

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

        return "redirect:/studentAssignments/list?success";
    }

    // Download submitted assignment
    @GetMapping("/submittedPdf/{id}")
    public ResponseEntity<byte[]> getSubmittedPdf(@PathVariable Long id, HttpSession session) {
        UserMaster student = (UserMaster) session.getAttribute("user");
        if (student == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        AdmissionMaster admission = student.getAdmissions().isEmpty() ? null : student.getAdmissions().get(0);
        if (admission == null) return ResponseEntity.notFound().build();

        SubmitAssignment submission = submitAssignmentRepository
                .findByAssignmentAndAdmission(assignmentRepository.findById(id).orElse(null), admission)
                .orElse(null);

        if (submission == null || submission.getPdf() == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"submission.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(submission.getPdf());
    }

    // Delete submitted assignment within 24 hours
    @PostMapping("/delete/{id}")
    public String deleteSubmission(@PathVariable Long id, HttpSession session) {
        UserMaster student = (UserMaster) session.getAttribute("user");
        if (student == null || student.getRole() != UserMaster.Role.STUDENT) return "error";

        AdmissionMaster admission = (AdmissionMaster) admissionRepository.findByUserMaster_UserId(student.getUserId());
        if (admission == null) return "error";

        AssignmentMaster assignment = assignmentRepository.findById(id).orElse(null);
        if (assignment == null) return "error";

        SubmitAssignment submission = submitAssignmentRepository.findByAssignmentAndAdmission(assignment, admission).orElse(null);
        if (submission != null) {
            long diffMillis = System.currentTimeMillis() - submission.getSubmittedAt().getTime();
            if (diffMillis < 86400000) {
                submitAssignmentRepository.delete(submission);
                return "redirect:/studentAssignments/list?deleted";
            } else {
                return "redirect:/studentAssignments/list?error=deleteTimeExpired";
            }
        }

        return "redirect:/studentAssignments/list?error=notFound";
    }
}
