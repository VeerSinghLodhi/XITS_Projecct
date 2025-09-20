package com.example.SamvaadProject.attendancepackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "attendance_master")
public class AttendanceMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "admissionId")
    private AdmissionMaster admission;

    private String status; // Present, Absent, Leave
    private Date attendanceDate;

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public AdmissionMaster getAdmission() {
        return admission;
    }

    public void setAdmission(AdmissionMaster admission) {
        this.admission = admission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
}

