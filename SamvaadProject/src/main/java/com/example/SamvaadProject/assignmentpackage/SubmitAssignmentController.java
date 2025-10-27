package com.example.SamvaadProject.assignmentpackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SubmitAssignmentController {

    @Autowired
    SubmitRepository submitRepository;


    @GetMapping("/getSubmissionsByAssignment/{assignmentId}")
    @ResponseBody
    public List<SubmitAssignmentDTO> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        List<SubmitAssignment> submissions = submitRepository.findByAssignment_AssignmentId(assignmentId);

        // Convert to DTOs
        return submissions.stream().map(submission -> {
            String studentName = "Unknown";

            try {
                if (submission.getAdmission() != null &&
                        submission.getAdmission().getUserMaster() != null &&
                        submission.getAdmission().getUserMaster().getFullName() != null) {
                    studentName = submission.getAdmission().getUserMaster().getFullName();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new SubmitAssignmentDTO(
                    submission.getSubmitId(),
                    studentName,
                    submission.getSubmittedAt()
            );
        }).collect(Collectors.toList());
    }

//    @GetMapping("/downloadSubmission/{submissionId}")
//    public ResponseEntity<byte[]> downloadSubmission(@PathVariable Long submissionId) {
//        SubmitAssignment submission = submitRepository.findById(submissionId)
//                .orElseThrow(() -> new RuntimeException("Submission not found"));
//
//        return ResponseEntity.ok()
//                .header("Content-Disposition", "attachment; filename=submitted_assignment_" + submissionId + ".pdf")
//                .body(submission.getPdf());
//    }
        @GetMapping("/downloadSubmission/{submissionId}")
        public ResponseEntity<byte[]> viewSubmission(@PathVariable Long submissionId) {
            SubmitAssignment submission = submitRepository.findById(submissionId)
                    .orElseThrow(() -> new RuntimeException("Submission not found"));

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "inline; filename=submitted_assignment_" + submissionId + ".pdf")
                    .body(submission.getPdf());
        }

}
