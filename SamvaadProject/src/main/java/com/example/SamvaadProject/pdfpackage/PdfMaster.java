package com.example.SamvaadProject.pdfpackage;

import com.example.SamvaadProject.coursepackage.CourseMaster;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "pdf_master")
public class PdfMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdfId;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private CourseMaster course;

    @Column(name="documentname")
    private String documentName;

    private Date uploadedAt;


//    @ManyToOne
//    @JoinColumn(name = "batchId")
//    private PdfMaster pdfs;

    private byte [] documentPath;

    public String getDocumentName() {
        return documentName;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Long getPdfId() {
        return pdfId;
    }

    public void setPdfId(Long pdfId) {
        this.pdfId = pdfId;
    }

    public CourseMaster getCourse() {
        return course;
    }

    public void setCourse(CourseMaster course) {
        this.course = course;
    }

    public byte[] getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(byte[] documentPath) {
        this.documentPath = documentPath;
    }
}


