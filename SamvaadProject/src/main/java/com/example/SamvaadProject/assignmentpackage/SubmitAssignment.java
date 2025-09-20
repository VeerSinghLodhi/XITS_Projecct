package com.example.SamvaadProject.assignmentpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import jakarta.persistence.*;

@Entity
@Table(name = "submit_assignment")
public class SubmitAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submitId;

    @ManyToOne
    @JoinColumn(name = "assignmentId")
    private AssignmentMaster assignment;

    @ManyToOne
    @JoinColumn(name = "admissionId")
    private AdmissionMaster admission;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserMaster professor; // Linked with UserMaster

    public Long getSubmitId() {
        return submitId;
    }

    public void setSubmitId(Long submitId) {
        this.submitId = submitId;
    }

    public AssignmentMaster getAssignment() {
        return assignment;
    }

    public void setAssignment(AssignmentMaster assignment) {
        this.assignment = assignment;
    }

    public AdmissionMaster getAdmission() {
        return admission;
    }

    public void setAdmission(AdmissionMaster admission) {
        this.admission = admission;
    }

    public UserMaster getProfessor() {
        return professor;
    }

    public void setProfessor(UserMaster professor) {
        this.professor = professor;
    }
}


