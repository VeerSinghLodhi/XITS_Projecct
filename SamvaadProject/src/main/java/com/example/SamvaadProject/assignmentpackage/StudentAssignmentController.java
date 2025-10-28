package com.example.SamvaadProject.assignmentpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import com.example.SamvaadProject.usermasterpackage.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

//    // List assignments
//    @GetMapping("/list")
//    public String listAssignments(HttpSession session, Model model) {
//        UserMaster student = (UserMaster) session.getAttribute("user");
//        if (student == null || student.getRole() != UserMaster.Role.STUDENT) {
//            model.addAttribute("error", "Unauthorized access");
//            return "error";
//        }
//
//        // Student batches
//        List<BatchMaster> batches = batchMasterRepository.findBatchesByStudent(student.getUserId());
//        List<Long> batchIds = batches.stream().map(BatchMaster::getBatchId).collect(Collectors.toList());
//
//
//        // Assignments in batches
//        List<AssignmentMaster> assignments = batchIds.isEmpty()
//                ? Collections.emptyList()
//                : assignmentRepository.findByBatch_BatchIdIn(batchIds);
//
//        // Submitted assignments
//        List<SubmitAssignment> submittedAssignments = submitAssignmentRepository
//                .findByAdmission_UserMaster_UserId(student.getUserId());
//
//        List<Long> submittedAssignmentIds = submittedAssignments.stream()
//                .map(sa -> sa.getAssignment().getAssignmentId())
//                .collect(Collectors.toList());
//
//        // Map to check delete eligibility (within 24 hours)
//        Map<Long, Boolean> deletableMap = submittedAssignments.stream()
//                .collect(Collectors.toMap(
//                        sa -> sa.getAssignment().getAssignmentId(),
//                        sa -> sa.getSubmittedAt() != null &&
//                                (System.currentTimeMillis() - sa.getSubmittedAt().getTime()) < 86400000
//                ));
//
//        model.addAttribute("assignments", assignments);
//        model.addAttribute("submittedAssignmentIds", submittedAssignmentIds);
//        model.addAttribute("deletableMap", deletableMap);
//
//        return "Student_assignment";
//    }

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


    // Download submitted assignment
    @GetMapping("/submittedPdf/{submitId}")
    public ResponseEntity<byte[]> getSubmittedPdf(@PathVariable("submitId") Long id, HttpSession session) {

//        AdmissionMaster admission = student.getAdmissions().isEmpty() ? null : student.getAdmissions().get(0);
//        if (admission == null) return ResponseEntity.notFound().build();

        SubmitAssignment submission = submitAssignmentRepository.findById(id).orElse(null);

        if (submission == null || submission.getPdf() == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"submission.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(submission.getPdf());
    }


    @Autowired
    private GeminiEvaluationService geminiEvaluationService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/submit")
    public String submitAssignment(@RequestParam("assignmentId") Long assignmentId,
                                   @RequestParam("file") MultipartFile file,
                                   HttpSession session,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        Long userId=(Long) session.getAttribute("userId");
        UserMaster student=userRepository.findById(userId).orElse(null);
        if(student==null){
            model.addAttribute("error","Session expired!");
            return "login";
        }

        AssignmentMaster assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        AdmissionMaster admission = student.getAdmissions().isEmpty()
                ? null
                : student.getAdmissions().get(0);

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
                if (diffMillis >= 2 * 86400000L) {
                    return "login";
                }
            }
            submission.setPdf(file.getBytes());
            submission.setSubmittedAt(new Date());
            submission.setGptFeedback(null);
            submission.setGptScore(null);
            submission.setGptEvaluatedAt(null);

            SubmitAssignment savedSubmission = submitAssignmentRepository.save(submission);
            redirectAttributes.addAttribute("submitId",savedSubmission.getSubmitId());
            geminiEvaluationService.evaluateAssignmentAsync(savedSubmission, assignment);
            System.out.println("Gemini evaluation triggered for submission ID: " + savedSubmission.getSubmitId());
        } catch (IOException e) {
            e.printStackTrace();
            return "login";
        }
        return "redirect:/student/dashboard#assignments";
    }

    @PostMapping("/delete/{assignmentId}")
    public String deleteSubmission(@PathVariable Long assignmentId, HttpSession session,Model model ) {
        Long userId=(Long) session.getAttribute("userId");
        UserMaster student=userRepository.findById(userId).orElse(null);
        if(student==null){
            model.addAttribute("error","Session expired!");
            return "login";
        }
        AdmissionMaster admission = student.getAdmissions().isEmpty() ? null : student.getAdmissions().get(0);
        if (admission == null) return "error";
        submitAssignmentRepository.findByAssignmentAndAdmission(
                assignmentRepository.findById(assignmentId).orElseThrow(),
                admission
        ).ifPresent(submitAssignmentRepository::delete);
        return "redirect:/student/dashboard#assignments";
    }


    private final ObjectMapper mapper = new ObjectMapper();

@GetMapping("/feedback/{submitId}")
@ResponseBody
public ResponseEntity<Map<String, Object>> getFeedbackData(@PathVariable Long submitId) {
    Map<String, Object> response = new HashMap<>();

    SubmitAssignment submission = submitAssignmentRepository.findById(submitId).orElse(null);
    if (submission == null) {
        response.put("status", "error");
        response.put("message", "No submission found for the provided ID.");
        return ResponseEntity.ok(response);
    }

    AssignmentMaster assignment = assignmentRepository
            .findById(submission.getAssignment().getAssignmentId())
            .orElse(null);

    if (assignment == null) {
        response.put("status", "error");
        response.put("message", "Assignment details for this submission were not found.");
        return ResponseEntity.ok(response);
    }

    response.put("assignmentName", assignment.getPdfName());
    response.put("batchName", assignment.getBatch().getName());
    response.put("submittedAt", submission.getSubmittedAt());

    String feedbackJson = submission.getGptFeedback();
    if (feedbackJson == null || feedbackJson.isBlank()) {
        response.put("status", "pending");
        response.put("summary", "Evaluation Pending or Not Yet Available.");
        return ResponseEntity.ok(response);
    }

    try {
        String cleanedJson = feedbackJson.trim();
        if (cleanedJson.startsWith("```json")) cleanedJson = cleanedJson.substring(7).trim();
        else if (cleanedJson.startsWith("```")) cleanedJson = cleanedJson.substring(3).trim();
        if (cleanedJson.endsWith("```")) cleanedJson = cleanedJson.substring(0, cleanedJson.length() - 3).trim();

        JsonNode root = mapper.readTree(cleanedJson);

        response.put("status", "success");
        response.put("summary", root.path("summary").asText("No summary provided."));
        response.put("verdict", root.path("Status").asText(root.path("verdict").asText("Unknown")));
        response.put("grade", root.path("grade").asInt(-1));

        List<String> issues = new ArrayList<>();
        if (root.has("issues") && root.get("issues").isArray()) {
            root.get("issues").forEach(i -> issues.add(i.asText()));
        }
        List<String> suggestions = new ArrayList<>();
        if (root.has("suggestions") && root.get("suggestions").isArray()) {
            root.get("suggestions").forEach(s -> suggestions.add(s.asText()));
        }

        response.put("issues", issues);
        response.put("suggestions", suggestions);

    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", "Invalid JSON or Parsing Error.");
        e.printStackTrace();
    }

    return ResponseEntity.ok(response);
}

}
