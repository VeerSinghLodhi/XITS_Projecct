package com.example.SamvaadProject.attendance_view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface attendanceview_repo extends JpaRepository<AttendanceView,Integer> {

    //@Query("SELECT a FROM Attendance_View a WHERE a.batchId=:batchId")
    List<AttendanceView> findByBatchId(Long batchId);
}
