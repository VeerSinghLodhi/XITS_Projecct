package com.example.SamvaadProject.attendance_view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_map")
public class    AttendanceView {

    @Id
    private Long user_id;

    private Long admission_id;
    @Column(name = "batch_id")
    private Long batchId;
    private String full_name;

    public AttendanceView(Long user_id, Long admission_id, Long batch_id, String full_name) {
        this.user_id = user_id;
        this.admission_id = admission_id;
        this.batchId = batch_id;
        this.full_name = full_name;
    }

    public AttendanceView() {
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getAdmission_id() {
        return admission_id;
    }

    public void setAdmission_id(Long admission_id) {
        this.admission_id = admission_id;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
