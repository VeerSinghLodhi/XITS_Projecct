package com.example.SamvaadProject.studentbatchpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentBatchRepository extends JpaRepository<StudentBatchMap,Long> {
    List<StudentBatchMap> findByBatch_BatchId(Long batchId);
    StudentBatchMap findByAdmission_AdmissionId(String admissionID);
    List<StudentBatchMap> findByBatch_Course_CourseId(Long courseId);
//    List<AdmissionMaster> findByUserMaster_UserId(Long userId);

//    @Query("SELECT sbm.batch FROM StudentBatchMap sbm " +
//            "JOIN sbm.admission a " +
//            "WHERE a.userMaster.userId = :userId")
//        List<BatchMaster> findBatchesByStudentUserId(@Param("userId") Long userId);
@Query("SELECT sbm.batch FROM StudentBatchMap sbm " +
        "JOIN sbm.admission a " +
        "WHERE a.userMaster.userId = :userId " +
        "ORDER BY CASE WHEN sbm.batch.status = 'ARCHIVED' THEN 1 ELSE 0 END, sbm.batch.batchId DESC")
List<BatchMaster> findBatchesByStudentUserId(@Param("userId") Long userId);

}
