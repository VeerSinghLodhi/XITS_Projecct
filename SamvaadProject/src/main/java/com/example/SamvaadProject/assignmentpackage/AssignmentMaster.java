package com.example.SamvaadProject.assignmentpackage;

import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "assignment_master")
public class AssignmentMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    private byte [] pdfs;

    @ManyToOne
    @JoinColumn(name = "batchId")
    private BatchMaster batch;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserMaster professor;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private List<SubmitAssignment> submissions;

    @Column(name = "pdf_date")
    private LocalDate pdfDate;

    @Column(name = "pdf_name")
    private String pdfName;

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public byte[] getPdfs() {
        return pdfs;
    }

    public void setPdfs(byte[] pdfs) {
        this.pdfs = pdfs;
    }

    public BatchMaster getBatch() {
        return batch;
    }

    public void setBatch(BatchMaster batch) {
        this.batch = batch;
    }

    public UserMaster getProfessor() {
        return professor;
    }

    public void setProfessor(UserMaster professor) {
        this.professor = professor;
    }

    public List<SubmitAssignment> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<SubmitAssignment> submissions) {
        this.submissions = submissions;
    }

    public LocalDate getPdfDate() {
        return pdfDate;
    }

    public void setPdfDate(LocalDate pdfDate) {
        this.pdfDate = pdfDate;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public AssignmentMaster() {
    }

    public AssignmentMaster(Long assignmentId, byte[] pdfs, BatchMaster batch, UserMaster professor, List<SubmitAssignment> submissions, LocalDate pdfDate, String pdfName) {
        this.assignmentId = assignmentId;
        this.pdfs = pdfs;
        this.batch = batch;
        this.professor = professor;
        this.submissions = submissions;
        this.pdfDate = pdfDate;
        this.pdfName = pdfName;
    }
}

