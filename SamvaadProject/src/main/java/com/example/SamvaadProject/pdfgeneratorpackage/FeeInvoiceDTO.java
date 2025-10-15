package com.example.SamvaadProject.pdfgeneratorpackage;

import java.time.LocalDate;

public class FeeInvoiceDTO {
    private Long invoiceId;
    private String studentName;
    private Long admissionId;
    private String courseName;
    private String feeCategory;
    private Double amount;
    private String paymentMode;
    private LocalDate paymentDate;

    // Constructors
    public FeeInvoiceDTO() {}

    public FeeInvoiceDTO( String studentName, Long admissionId, String courseName,
                         String feeCategory, Double amount, String paymentMode, LocalDate paymentDate) {
        this.studentName = studentName;
        this.admissionId = admissionId;
        this.courseName = courseName;
        this.feeCategory = feeCategory;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.paymentDate = paymentDate;
    }

    // Getters & Setters
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Long getAdmissionId() { return admissionId; }
    public void setAdmissionId(Long admissionId) { this.admissionId = admissionId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getFeeCategory() { return feeCategory; }
    public void setFeeCategory(String feeCategory) { this.feeCategory = feeCategory; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
