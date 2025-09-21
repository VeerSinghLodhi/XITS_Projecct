package com.example.SamvaadProject.feespackage;

public class FeeDTO {

    private Long feeId;
    private String admissionId;
    private Double amount;
    private String paymentDate;
    private String paymentMode;
    private String courseName;

    public FeeDTO(Long feeId, String admissionId, Double amount, String paymentDate, String paymentMode, String courseName) {
        this.feeId = feeId;
        this.admissionId = admissionId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMode = paymentMode;
        this.courseName = courseName;
    }

    public FeeDTO(Long feeId, String admissionId, Double amount, String paymentDate, String paymentMode) {
        this.feeId = feeId;
        this.admissionId = admissionId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMode = paymentMode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
