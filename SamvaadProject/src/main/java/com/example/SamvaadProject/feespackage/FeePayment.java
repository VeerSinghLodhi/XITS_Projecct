package com.example.SamvaadProject.feespackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "fees")
public class FeePayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feeId;

    @ManyToOne
    @JoinColumn(name = "admissionId")
    private AdmissionMaster admission;

    private Double amount;
    private Date paymentDate;
    private String paymentMode; // Cash, Online, Cheque, Card

    // Getters & Setters

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }

    public AdmissionMaster getAdmission() {
        return admission;
    }

    public void setAdmission(AdmissionMaster admission) {
        this.admission = admission;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
