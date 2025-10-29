package com.example.SamvaadProject.assignmentpackage;

import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentMaster,Long> {
    List<AssignmentMaster> findByPdfDateBefore(LocalDate date);
    List<AssignmentMaster> findByBatch_BatchIdIn(List<Long> batchIds);
//    List<AssignmentMaster> findByBatch_BatchIdIn(Long batchId);
    @Query("Select a from AssignmentMaster a where a.batch.batchId=:batchId")
    List<AssignmentMaster>getAllAssignmentByBatchId(@Param("batchId")Long batchId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AssignmentMaster a WHERE a.assignmentId = :assignmentId")
    void getAssignmentDelete(@Param("assignmentId") Long assignmentId);

    List<AssignmentMaster> findByBatch_BatchId(Long batchId);

    @Query("""
        SELECT COUNT(a)
        FROM AssignmentMaster a
        WHERE a.batch.batchId IN (
            SELECT sbm.batch.batchId
            FROM StudentBatchMap sbm
            WHERE sbm.admission.userMaster.userId = :userId
        )
    """)
    Long countAssignmentsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(a) FROM AssignmentMaster a WHERE a.professor=:professor")
    Long getCountAssignmentsByFaculty(@Param("professor")UserMaster userMaster);

}
