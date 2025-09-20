package com.example.SamvaadProject.coursepackage;


import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.enquirypackage.EnquiryMaster;
import com.example.SamvaadProject.pdfpackage.PdfMaster;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "course_master")
public class CourseMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotBlank(message = "Duration is required")
//    @Pattern(regexp = "^[0-9]+\\s+(Months|Weeks|Days)$", message = "Duration must be like '3 Months', '6 Weeks'")
    private String duration;

    @Pattern(regexp = "^[0-9]+$", message = "Lump sum must be numeric")
    private String lumpSum;

    @NotNull(message = "Installment is required")
    @DecimalMin(value = "100.0", message = "Installment must be at least 100")

    private Double installment;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<EnquiryMaster> enquiries;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<BatchMaster> batches;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<AdmissionMaster> admissions;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<PdfMaster> pdfs;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLumpSum() {
        return lumpSum;
    }

    public List<PdfMaster> getPdfs() {
        return pdfs;
    }

    public void setPdfs(List<PdfMaster> pdfs) {
        this.pdfs = pdfs;
    }

    public void setLumpSum(String lumpSum) {
        this.lumpSum = lumpSum;
    }

    public Double getInstallment() {
        return installment;
    }

    public void setInstallment(Double installment) {
        this.installment = installment;
    }

    public List<EnquiryMaster> getEnquiries() {
        return enquiries;
    }

    public void setEnquiries(List<EnquiryMaster> enquiries) {
        this.enquiries = enquiries;
    }

    public List<BatchMaster> getBatches() {
        return batches;
    }

    public void setBatches(List<BatchMaster> batches) {
        this.batches = batches;
    }

    public List<AdmissionMaster> getAdmissions() {
        return admissions;
    }

    public void setAdmissions(List<AdmissionMaster> admissions) {
        this.admissions = admissions;
    }
}
