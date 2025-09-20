package com.example.SamvaadProject.admissionpackage;

public class AdmissionDTO {
    private String admissionId;
    private Double fees;
    private Double discount;
    private Double balance;
    private Long courseId;
    private String courseName;
    private String studentName;
    private String admissionDate;
    private String batchName;

    public AdmissionDTO(String admissionId, String studentName,Long courseId) {
        this.admissionId = admissionId;
        this.studentName = studentName;
        this.courseId=courseId;
    }

    public AdmissionDTO(String admissionId, String courseName, String batchName, String joiningDate) {
        this.admissionId = admissionId;
        this.courseName = courseName;
        this.batchName = batchName;
        this.admissionDate = joiningDate;
    }
    //    ad.getAdmissionId(),
//            ad.getCourse().getCourseName(),
//                        ad.getBatch().getBatchName(),
//                        ad.getJoinDate().toString()

    public AdmissionDTO() {
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getAdmissionId() {
        return admissionId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
