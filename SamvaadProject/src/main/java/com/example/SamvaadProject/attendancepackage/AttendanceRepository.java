package com.example.SamvaadProject.attendancepackage;

import com.example.SamvaadProject.attendance_view.AttendanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceMaster, Long> {
    Optional<AttendanceMaster> findByAdmission_AdmissionIdAndAttendanceDate(String admissionId, Date attendanceDate);

    @Query("SELECT a FROM AttendanceMaster a " + "WHERE a.attendanceDate = :attendanceDate " + "AND a.admission.admissionId IN :admissionIds ORDER BY a.admission.admissionId ASC")
    List<AttendanceMaster> findByAttendanceDateAndAdmissionIds(@Param("attendanceDate") Date attendanceDate, @Param("admissionIds") List<String> admissionIds);

//    @Query("SELECT a FROM AttendanceMaster a  WHERE  a.admission.admissionId IN :admissionIds  ORDER BY a.attendanceDate ASC")
//    List<AttendanceMaster> findByAdmissionIds( @Param("admissionIds") List<String> admissionIds);

    @Query("SELECT a FROM AttendanceMaster a WHERE a.admission.admissionId IN :admissionIds")
    List<AttendanceMaster> findByAdmissionIds(@Param("admissionIds") List<String> admissionIds);


    @Query("SELECT a FROM AttendanceMaster a WHERE a.attendanceDate = ( SELECT MAX(am.attendanceDate) FROM AttendanceMaster am WHERE am.admission.admissionId = a.admission.admissionId) AND a.admission.admissionId IN :admissionIds")
    List<AttendanceMaster> findLatestByAdmissionIds(@Param("admissionIds") List<String> admissionIds);

    @Query("SELECT Count(a) FROM  AttendanceMaster a WHERE a.admission.admissionId IN :admissionIds")
    Long findByTotalAttendanceCount(@Param("admissionIds") List<String>admissionIds);

    @Query("SELECT Count(a) FROM AttendanceMaster a WHERE a.admission.admissionId IN :admissionIds AND a.status='Present'")
    Long findByCount(@Param("admissionIds") List<String>admissionIds);

    @Query("SELECT a.admission.admissionId, COUNT(a) " +
            "FROM AttendanceMaster a " +
            "WHERE a.admission.admissionId IN :admissionIds AND a.status='Present' " +
            "GROUP BY a.admission.admissionId")
    List<Object[]> findPresentCountPerAdmission(@Param("admissionIds") List<String> admissionIds);


    @Query("""
       SELECT COUNT(DISTINCT a.attendanceDate)
       FROM AttendanceMaster a
       JOIN StudentBatchMap sbm ON sbm.admission.admissionId = a.admission.admissionId
       WHERE sbm.batch.batchId = :batchId
       """)
    Long countDistinctSessionsByBatchId(@Param("batchId") Long batchId);

    @Query("SELECT new com.example.SamvaadProject.attendancepackage.AttendanceDTO(" +
            "a.admission.admissionId, " +
            "a.status, " +
            "CAST(a.attendanceDate AS string)) " +
            "FROM AttendanceMaster a " +
            "JOIN a.admission.studentBatchMappings sbm " +
            "WHERE sbm.batch.batchId = :batchId " +
            "AND a.admission.userMaster.userId = :userId")
    List<AttendanceDTO> findAttendanceByUserIdAndBatchId(@Param("userId") Long userId,
                                                         @Param("batchId") Long batchId);

}
