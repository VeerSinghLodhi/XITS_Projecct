package com.example.SamvaadProject.admissionpackage;

import com.example.SamvaadProject.assignmentpackage.SubmitAssignment;
import com.example.SamvaadProject.attendancepackage.AttendanceMaster;
import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.enquirypackage.EnquiryMaster;
import com.example.SamvaadProject.feespackage.FeePayment;
import com.example.SamvaadProject.studentbatchpackage.StudentBatchMap;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "admission_master")
public class AdmissionMaster {

    @Id
    private String admissionId;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private CourseMaster course;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserMaster userMaster;

    private Double fees;
    private Double discount;
    private Date joinDate;

//    private Double netfees;
    private Double balance;

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL)
    private List<StudentBatchMap> studentBatchMappings;

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL)
    private List<SubmitAssignment> submittedAssignments;

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL)
    private List<FeePayment> feePayments;

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL)
    private List<AttendanceMaster> attendances;

    public AdmissionMaster() {
    }

    // List lst(1,2,3,4,5);
    // lst.get(0);

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public CourseMaster getCourse() {
        return course;
    }

    public void setCourse(CourseMaster course) {
        this.course = course;
    }

    public UserMaster getUserMaster() {
        return userMaster;
    }

    public void setUserMaster(UserMaster userMaster) {
        this.userMaster = userMaster;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public List<StudentBatchMap> getStudentBatchMappings() {
        return studentBatchMappings;
    }

    public void setStudentBatchMappings(List<StudentBatchMap> studentBatchMappings) {
        this.studentBatchMappings = studentBatchMappings;
    }

    public List<SubmitAssignment> getSubmittedAssignments() {
        return submittedAssignments;
    }

    public void setSubmittedAssignments(List<SubmitAssignment> submittedAssignments) {
        this.submittedAssignments = submittedAssignments;
    }

    public List<FeePayment> getFeePayments() {
        return feePayments;
    }

    public void setFeePayments(List<FeePayment> feePayments) {
        this.feePayments = feePayments;
    }

    public List<AttendanceMaster> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<AttendanceMaster> attendances) {
        this.attendances = attendances;
    }

//    public Double getNetfees() {
//        return netfees;
//    }
//
//    public void setNetfees(Double netfees) {
//        this.netfees = netfees;
//    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
