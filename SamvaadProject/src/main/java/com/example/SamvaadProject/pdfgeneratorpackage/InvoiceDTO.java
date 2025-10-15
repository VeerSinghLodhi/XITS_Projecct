package com.example.SamvaadProject.pdfgeneratorpackage;

import java.util.List;

public class InvoiceDTO {

    String studentName;
    String admissionId;
    String courseName;
    String feeCategory;
    String registrationFee;
    String courseFee;
    String discount;
    String netPayableAmount;
    String pendingBalance;
    List<FeePaymentDTO>allInstallments;
    String totalInstallments;

    public InvoiceDTO() {
    }

    public InvoiceDTO(String studentName, String admissionId, String courseName, String feeCategory, String registrationFee, String courseFee, String discount, String netPayableAmount, String pendingBalance, List<FeePaymentDTO> allInstallments,String totalInstallments) {
        this.studentName = studentName;
        this.admissionId = admissionId;
        this.courseName = courseName;
        this.feeCategory = feeCategory;
        this.registrationFee = registrationFee;
        this.courseFee = courseFee;
        this.discount = discount;
        this.netPayableAmount = netPayableAmount;
        this.pendingBalance = pendingBalance;
        this.allInstallments = allInstallments;
        this.totalInstallments=totalInstallments;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTotalInstallments() {
        return totalInstallments;
    }

    public void setTotalInstallments(String totalInstallments) {
        this.totalInstallments = totalInstallments;
    }

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFeeCategory() {
        return feeCategory;
    }

    public void setFeeCategory(String feeCategory) {
        this.feeCategory = feeCategory;
    }

    public String getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(String registrationFee) {
        this.registrationFee = registrationFee;
    }

    public String getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(String courseFee) {
        this.courseFee = courseFee;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getNetPayableAmount() {
        return netPayableAmount;
    }

    public void setNetPayableAmount(String netPayableAmount) {
        this.netPayableAmount = netPayableAmount;
    }

    public String getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(String pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public List<FeePaymentDTO> getAllInstallments() {
        return allInstallments;
    }

    public void setAllInstallments(List<FeePaymentDTO> allInstallments) {
        this.allInstallments = allInstallments;
    }
}
