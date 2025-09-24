package com.example.SamvaadProject.batchmasterpackage;

import java.time.LocalDateTime;

public class BatchDTO {
        private Long batchId;
        private String startDate;
        private String status; // Active, Archived
        private String mode;
        private Long courseId;
        private Long facultyId;
        private String courseName;
        private String batchName;

    public BatchDTO() {
    }

    public BatchDTO(Long batchId, String startDate, String status, String mode, Long courseId, Long facultyId,String courseName ,String batchName){
        this.batchId = batchId;
        this.startDate = startDate;
        this.status = status;
        this.mode = mode;
        this.courseId = courseId;
        this.facultyId = facultyId;
        this.courseName=courseName;
        this.batchName=batchName;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }
}
