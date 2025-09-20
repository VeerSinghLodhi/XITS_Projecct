package com.example.SamvaadProject.batchmasterpackage;

import com.example.SamvaadProject.assignmentpackage.AssignmentMaster;
import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.pdfpackage.PdfMaster;
import com.example.SamvaadProject.studentbatchpackage.StudentBatchMap;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "batch_master")
public class BatchMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private CourseMaster course;//

    private String name;//veer
    private LocalDateTime startDate;
    private LocalDate endDate;
    private String status; // Active, Completed, Pending
    private String mode;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserMaster faculty; // Linked with UserMaster

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<StudentBatchMap> studentBatchMappings;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<AssignmentMaster> assignments;

//    @OneToMany(mappedBy = "pdfs", cascade = CascadeType.ALL)
//    private List<PdfMaster> pdfs;

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public CourseMaster getCourse() {
        return course;
    }

    public void setCourse(CourseMaster course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public UserMaster getFaculty() {
        return faculty;
    }

    public void setFaculty(UserMaster faculty) {
        this.faculty = faculty;
    }

    public List<StudentBatchMap> getStudentBatchMappings() {
        return studentBatchMappings;
    }

    public void setStudentBatchMappings(List<StudentBatchMap> studentBatchMappings) {
        this.studentBatchMappings = studentBatchMappings;
    }

    public List<AssignmentMaster> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignmentMaster> assignments) {
        this.assignments = assignments;
    }

//    public List<PdfMaster> getPdfs() {
//        return pdfs;
//    }
//
//    public void setPdfs(List<PdfMaster> pdfs) {
//        this.pdfs = pdfs;
//    }
}

