package com.example.SamvaadProject.assignmentpackage;
import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.assignmentpackage.AssignmentMaster;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.List;

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
    private UserMaster professor;

    @Column(name = "pdf")
    private byte[] pdf;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submitted_at")
    private Date submittedAt;

    //    @Column(name = "gpt_feedback", columnDefinition = "TEXT")
//    private String gptFeedback;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "gpt_feedback", columnDefinition = "jsonb")
    private String gptFeedback;

    @Column(name = "extra_feedback")
    private String extrafeedback;

    @Column(name = "gpt_score")
    private Integer gptScore;

    @Column(name = "status")
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gpt_evaluated_at")
    private Date gptEvaluatedAt;

    @Transient
    private Integer gptFeedbackScore;

    @Transient
    private Boolean gptFeedbackPass;

    @Transient
    private List<String> gptSuggestions;

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

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getGptFeedback() {
        return gptFeedback;
    }

    public void setGptFeedback(String gptFeedback) {
        this.gptFeedback = gptFeedback;
    }

    public String getExtrafeedback() {
        return extrafeedback;
    }

    public void setExtrafeedback(String extrafeedback) {
        this.extrafeedback = extrafeedback;
    }

    public Integer getGptScore() {
        return gptScore;
    }

    public void setGptScore(Integer gptScore) {
        this.gptScore = gptScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getGptEvaluatedAt() {
        return gptEvaluatedAt;
    }

    public void setGptEvaluatedAt(Date gptEvaluatedAt) {
        this.gptEvaluatedAt = gptEvaluatedAt;
    }

    public Integer getGptFeedbackScore() {
        return gptFeedbackScore;
    }

    public void setGptFeedbackScore(Integer gptFeedbackScore) {
        this.gptFeedbackScore = gptFeedbackScore;
    }

    public Boolean getGptFeedbackPass() {
        return gptFeedbackPass;
    }

    public void setGptFeedbackPass(Boolean gptFeedbackPass) {
        this.gptFeedbackPass = gptFeedbackPass;
    }

    public List<String> getGptSuggestions() {
        return gptSuggestions;
    }

    public void setGptSuggestions(List<String> gptSuggestions) {
        this.gptSuggestions = gptSuggestions;
    }
}

//package com.example.SamvaadProject.assignmentpackage;
//
//import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
//import com.example.SamvaadProject.usermasterpackage.UserMaster;
//import jakarta.persistence.*;
//
//import java.util.Date;
//
//@Entity
//@Table(name = "submit_assignment")
//public class SubmitAssignment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long submitId;
//
//    @ManyToOne
//    @JoinColumn(name = "assignmentId")
//    private AssignmentMaster assignment;
//
//    @ManyToOne
//    @JoinColumn(name = "admissionId")
//    private AdmissionMaster admission;
//
//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private UserMaster professor;
//
//    @Column(name = "pdf")
//    private byte[] pdf;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "submitted_at")
//    private Date submittedAt;
//
//    public Date getSubmittedAt() {
//        return submittedAt;
//    }
//
//    public void setSubmittedAt(Date submittedAt) {
//        this.submittedAt = submittedAt;
//    }
//
//    public SubmitAssignment() {
//    }
//
//    public SubmitAssignment(Long submitId, AssignmentMaster assignment, AdmissionMaster admission, UserMaster professor, byte[] pdf) {
//        this.submitId = submitId;
//        this.assignment = assignment;
//        this.admission = admission;
//        this.professor = professor;
//        this.pdf = pdf;
//    }
//
//    public byte[] getPdf() {
//        return pdf;
//    }
//
//    public void setPdf(byte[] pdf) {
//        this.pdf = pdf;
//    }
//
//    public Long getSubmitId() {
//        return submitId;
//    }
//
//    public void setSubmitId(Long submitId) {
//        this.submitId = submitId;
//    }
//
//    public AssignmentMaster getAssignment() {
//        return assignment;
//    }
//
//    public void setAssignment(AssignmentMaster assignment) {
//        this.assignment = assignment;
//    }
//
//    public AdmissionMaster getAdmission() {
//        return admission;
//    }
//
//    public void setAdmission(AdmissionMaster admission) {
//        this.admission = admission;
//    }
//
//    public UserMaster getProfessor() {
//        return professor;
//    }
//
//    public void setProfessor(UserMaster professor) {
//        this.professor = professor;
//    }
//}
//
//
