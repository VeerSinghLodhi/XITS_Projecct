package com.example.SamvaadProject.batchmasterpackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchMasterRepository extends JpaRepository<BatchMaster, Long> {

    @Query("SELECT b FROM BatchMaster b " +
            "WHERE b.course.courseId IN (" +
            "   SELECT a.course.courseId FROM AdmissionMaster a " +
            "   WHERE a.userMaster.userId = :studentId)")
    List<BatchMaster> findBatchesByStudent(Long studentId);

    @Query("Select b from BatchMaster b where b.faculty.userId=:userId and b.status=:status")
    List<BatchMaster>getAllBatchesByFaculty(@Param("userId")Long userId,@Param("status")String status);

    @Query("SELECT b FROM BatchMaster b WHERE b.faculty.userId = :facultyId ")
    List<BatchMaster> findByFacultyId(@Param("facultyId") Long facultyId);

    @Query("SELECT b FROM BatchMaster b WHERE b.course.courseId IN :courseIds")
    List<BatchMaster> findByCourseId(@Param("courseIds") List<Long>courseIds);

    @Query("SELECT b From BatchMaster b where b.status=:status")
    List<BatchMaster>findAllBatchByStatus(@Param("status")String status);

}
