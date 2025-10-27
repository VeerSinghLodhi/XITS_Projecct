package com.example.SamvaadProject.assignmentpackage;


import java.util.Date;

public class SubmitAssignmentDTO {
    private Long submissionId;
    private String studentName;
    private Date submissionDate;

    public SubmitAssignmentDTO(Long submissionId, String studentName, Date submissionDate) {
        this.submissionId = submissionId;
        this.studentName = studentName;
        this.submissionDate = submissionDate;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public String getStudentName() {
        return studentName;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }
}