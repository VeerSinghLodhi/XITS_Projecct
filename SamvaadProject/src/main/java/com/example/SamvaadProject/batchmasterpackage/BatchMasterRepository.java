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

    @Query("Select b from BatchMaster b where b.faculty.userId=:userId")
    List<BatchMaster>getAllBatchesByFaculty(@Param("userId")Long userId);
}
