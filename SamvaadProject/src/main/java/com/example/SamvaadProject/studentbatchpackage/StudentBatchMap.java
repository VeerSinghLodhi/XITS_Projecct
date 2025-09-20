package com.example.SamvaadProject.studentbatchpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import jakarta.persistence.*;

@Entity
@Table(name = "student_batch_map")
public class StudentBatchMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentBatchId;

    @ManyToOne
    @JoinColumn(name = "batchId")
    private BatchMaster batch;

    @ManyToOne
    @JoinColumn(name = "admissionId")
    private AdmissionMaster admission;

    public Long getStudentBatchId() {
        return studentBatchId;
    }

    public void setStudentBatchId(Long studentBatchId) {
        this.studentBatchId = studentBatchId;
    }

    public BatchMaster getBatch() {
        return batch;
    }

    public void setBatch(BatchMaster batch) {
        this.batch = batch;
    }

    public AdmissionMaster getAdmission() {
        return admission;
    }

    public void setAdmission(AdmissionMaster admission) {
        this.admission = admission;
    }
}

