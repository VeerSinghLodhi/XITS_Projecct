package com.example.SamvaadProject.enquirypackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.followuppackage.FollowUp;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "enquiry_master")
public class EnquiryMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enquiryId;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private CourseMaster course;

    private String studentName;
    private String qualification;
    private String collegeSchool;
    private String classSem;
    private Date enquiryDate;
    private Date edoj;
    private String address;
    private String cityState;
    private String referenceSource;
    private String mode;
    private String contactNo;
    private String email;
    private String branch;
    private long fees;
    private String type;

    @Column(length = 500)
    private String comments;

    @OneToMany(mappedBy = "enquiry", cascade = CascadeType.ALL)
    private List<FollowUp> followups;

//    @OneToMany(mappedBy = "enquiry", cascade = CascadeType.ALL)
//    private List<AdmissionMaster> admissions;


    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Long getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(Long enquiryId) {
        this.enquiryId = enquiryId;
    }

    public CourseMaster getCourse() {
        return course;
    }

    public void setCourse(CourseMaster course) {
        this.course = course;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getCollegeSchool() {
        return collegeSchool;
    }

    public void setCollegeSchool(String collegeSchool) {
        this.collegeSchool = collegeSchool;
    }

    public String getClassSem() {
        return classSem;
    }

    public void setClassSem(String classSem) {
        this.classSem = classSem;
    }

    public Date getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(Date enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public Date getEdoj() {
        return edoj;
    }

    public void setEdoj(Date edoj) {
        this.edoj = edoj;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getReferenceSource() {
        return referenceSource;
    }

    public void setReferenceSource(String referenceSource) {
        this.referenceSource = referenceSource;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public long getFees() {
        return fees;
    }

    public void setFees(long fees) {
        this.fees = fees;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<FollowUp> getFollowups() {
        return followups;
    }

    public void setFollowups(List<FollowUp> followups) {
        this.followups = followups;
    }
}

