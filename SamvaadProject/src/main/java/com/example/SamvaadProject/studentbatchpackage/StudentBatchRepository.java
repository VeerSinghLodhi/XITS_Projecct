package com.example.SamvaadProject.studentbatchpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentBatchRepository extends JpaRepository<StudentBatchMap,Long> {
    List<StudentBatchMap> findByBatch_BatchId(Long batchId);
    StudentBatchMap findByAdmission_AdmissionId(String admissionID);
    List<StudentBatchMap> findByBatch_Course_CourseId(Long courseId);
//    List<AdmissionMaster> findByUserMaster_UserId(Long userId);
}
