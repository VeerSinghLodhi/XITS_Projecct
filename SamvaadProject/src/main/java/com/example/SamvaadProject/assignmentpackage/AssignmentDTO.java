package com.example.SamvaadProject.assignmentpackage;

public class AssignmentDTO {

    private Long assignmentId;
    private String title;
    private String batchName;
    private String pdfName;
    private String pdfDate;

    public AssignmentDTO() {
    }


    public AssignmentDTO(Long assignmentId, String title, String batchName, String pdfName, String pdfDate) {
        this.title = title;
        this.batchName = batchName;
        this.pdfName = pdfName;
        this.pdfDate = pdfDate;
        this.assignmentId=assignmentId;
    }

    public String getPdfDate() {
        return pdfDate;
    }

    public void setPdfDate(String pdfDate) {
        this.pdfDate = pdfDate;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
}
