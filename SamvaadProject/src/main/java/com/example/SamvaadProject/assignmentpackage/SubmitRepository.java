package com.example.SamvaadProject.assignmentpackage;

import com.example.SamvaadProject.assignmentpackage.SubmitAssignment;
import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.assignmentpackage.AssignmentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmitRepository extends JpaRepository<SubmitAssignment, Long> {

    // Find all submissions for a student
    List<SubmitAssignment> findByAdmission_UserMaster_UserId(Long userId);

    // Find a submission for a specific assignment and admission
    Optional<SubmitAssignment> findByAssignmentAndAdmission(AssignmentMaster assignment, AdmissionMaster admission);
    List<SubmitAssignment> findByAssignment_AssignmentId(Long assignmentId);


    @Query("SELECT sa.assignment.assignmentId FROM SubmitAssignment sa WHERE sa.admission.admissionId = :admissionId")
    List<Long> findSubmittedAssignmentIdsByAdmissionId(@Param("admissionId") Long admissionId);

    @Query("""
        SELECT COUNT(s)
        FROM SubmitAssignment s
        WHERE s.admission.userMaster.userId = :userId
    """)
    Long countSubmissionsByUserId(@Param("userId") Long userId);

}
