package com.example.SamvaadProject.pdfpackage;

import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.coursepackage.CourseMaster;
import jakarta.persistence.*;

@Entity
@Table(name = "pdf_master")
public class PdfMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdfId;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private CourseMaster course;

//    @ManyToOne
//    @JoinColumn(name = "batchId")
//    private PdfMaster pdfs;

    private byte [] documentPath;

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


