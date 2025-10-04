package com.example.SamvaadProject.attendancepackage;



public class StudentDTO {

    private String admissionId;
    private String fullName;

    public StudentDTO(String admissionId, String fullName) {
        this.admissionId = admissionId;
        this.fullName = fullName;
    }

    public StudentDTO() {
    }

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
