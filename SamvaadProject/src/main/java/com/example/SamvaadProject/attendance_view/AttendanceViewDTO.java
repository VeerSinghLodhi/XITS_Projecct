package com.example.SamvaadProject.attendance_view;

public class AttendanceViewDTO {

    private Long admission_id;
    private String fullName;

    public AttendanceViewDTO() {
    }

    public AttendanceViewDTO(Long admission_id, String fullName) {
        this.admission_id = admission_id;
        this.fullName = fullName;
    }

    public Long getAdmission_id() {
        return admission_id;
    }

    public void setAdmission_id(Long admission_id) {
        this.admission_id = admission_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
