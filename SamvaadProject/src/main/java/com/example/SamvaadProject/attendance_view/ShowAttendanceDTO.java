package com.example.SamvaadProject.attendance_view;

public class ShowAttendanceDTO {

        private String admissionId;
        private String fullName;
        private Long presentCount;
        private Long absentCount;
        private double percentage;
        private String badgeClass;
        private String status;
        private Long totalAttendance;


    public ShowAttendanceDTO() {
    }

    public ShowAttendanceDTO(String admissionId, String fullName, Long presentCount, Long absentCount, double percentage, String badgeClass, String status, Long totalAttendance) {
        this.admissionId = admissionId;
        this.fullName = fullName;
        this.presentCount = presentCount;
        this.absentCount = absentCount;
        this.percentage = percentage;
        this.badgeClass = badgeClass;
        this.status = status;
        this.totalAttendance = totalAttendance;
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

    public Long getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(Long presentCount) {
        this.presentCount = presentCount;
    }

    public Long getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(Long absentCount) {
        this.absentCount = absentCount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public void setBadgeClass(String badgeClass) {
        this.badgeClass = badgeClass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTotalAttendance() {
        return totalAttendance;
    }

    public void setTotalAttendance(Long totalAttendance) {
        this.totalAttendance = totalAttendance;
    }
}
