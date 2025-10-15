package com.example.SamvaadProject.pdfgeneratorpackage;

public class FeePaymentDTO {

    String installmentNumber;
    String amount;
    String paymentMode;
    String paymentDate;
    String pendingAmount;

    public FeePaymentDTO() {
    }

    public FeePaymentDTO(String installmentNumber, String amount, String paymentMode, String paymentDate,String pendingAmount) {
        this.installmentNumber = installmentNumber;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.paymentDate = paymentDate;
        this.pendingAmount=pendingAmount;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(String installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
