package com.example.SamvaadProject.admissionpackage;

import com.example.SamvaadProject.assignmentpackage.SubmitAssignment;
import com.example.SamvaadProject.attendancepackage.AttendanceMaster;
import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.enquirypackage.EnquiryMaster;
import com.example.SamvaadProject.feespackage.FeePayment;
import com.example.SamvaadProject.studentbatchpackage.StudentBatchMap;
import com.example.SamvaadProject.usermasterpackage.UserMaster;
import jakarta.persistence.*;

import java.util.ArrayList;
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


    private Date joinDate;
    private String feeCategory;
    private Boolean registrationFeesPaid;
    private Integer noOfInstallments;
    private Double perInstallment;
    private Double registrationFee;


    private Double courseFee;
    private Double discount=0d;
    private Double netFees;

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL)
    private List<StudentBatchMap> studentBatchMappings;

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL)
    private List<SubmitAssignment> submittedAssignments;

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<FeePayment> feePayments =new ArrayList<>();

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

    public Integer getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(Integer noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }

    public Double getPerInstallment() {
        return perInstallment;
    }

    public void setPerInstallment(Double perInstallment) {
        this.perInstallment = perInstallment;
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


    public Double getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(Double courseFee) {
        this.courseFee = courseFee;
    }

    public Double getNetFees() {
        return netFees;
    }

    public void setNetFees(Double netFees) {
        this.netFees = netFees;
    }

    public String getFeeCategory() {
        return feeCategory;
    }

    public void setFeeCategory(String feeCategory) {
        this.feeCategory = feeCategory;
    }

    public Boolean getRegistrationFeesPaid() {
        return registrationFeesPaid;
    }

    public void setRegistrationFeesPaid(Boolean registrationFeesPaid) {
        this.registrationFeesPaid = registrationFeesPaid;
    }

    public Double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(Double registrationFee) {
        this.registrationFee = registrationFee;
    }

    @Transient
    public Double getLastBalanceAfterPayment() {


        if (!feePayments.isEmpty()) {
           // System.out.println("Ran for admission_id "+admissionId);
            return feePayments.get(feePayments.size() - 1).getBalanceAfterPayment();
        }
       // System.out.println("Net Fees "+netFees);
        return netFees;
    }

    @Transient
    public Integer getPendingInstallments() {
        if (noOfInstallments == null) return 0; // safe fallback
        if (!feePayments.isEmpty()) {
            return noOfInstallments - feePayments.get(feePayments.size() - 1).getInstallmentNo();
        }
        return noOfInstallments;
    }

//    @Transient
//    public Double getPendingAmount(){
//        if (!feePayments.isEmpty()) {
//            return noOfInstallments - feePayments.get(feePayments.size() - 1).getBalanceAfterPayment();
//        }
//        return netFees;
//    }


}
