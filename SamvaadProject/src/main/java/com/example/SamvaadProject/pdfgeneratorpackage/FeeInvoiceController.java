package com.example.SamvaadProject.pdfgeneratorpackage;


import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.feespackage.FeeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FeeInvoiceController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    AdmissionRepository admissionRepository;

    @Autowired
    FeeRepository feeRepository;

    @Autowired
    JavaMailSender mailSender;

    // Example: GET request that generates a PDF invoice from DTO
////    @GetMapping("/download/invoiceId")
////    public ResponseEntity<byte[]> downloadInvoice() throws IOException {
//    public void downloadInvoice(String admissionId) throws IOException {
//
//        InvoiceDTO invoiceDTO=new InvoiceDTO();
//        AdmissionMaster admissionMaster=admissionRepository.findById(admissionId).orElse(null);
//
//        invoiceDTO.setStudentName(admissionMaster.getUserMaster().getFullName());
//        invoiceDTO.setAdmissionId(admissionMaster.getAdmissionId());
//        invoiceDTO.setCourseName(admissionMaster.getCourse().getCourseName());
//        invoiceDTO.setFeeCategory(admissionMaster.getFeeCategory());
//        invoiceDTO.setRegistrationFee(admissionMaster.getRegistrationFee().toString());
//        invoiceDTO.setCourseFee(admissionMaster.getCourseFee().toString());
//        invoiceDTO.setDiscount(admissionMaster.getDiscount().toString());
//        invoiceDTO.setNetPayableAmount(admissionMaster.getNetFees()+"");
//        invoiceDTO.setPendingBalance(admissionMaster.getLastBalanceAfterPayment().toString());
//        invoiceDTO.setTotalInstallments(admissionMaster.getNoOfInstallments().toString());
//
//
//        System.out.println("Pending Amount "+invoiceDTO.getPendingBalance());
//        System.out.println("Net Payable Amount "+invoiceDTO.getNetPayableAmount());
//
//        List<FeePaymentDTO>feePaymentDTOS=feeRepository.findByAdmission_AdmissionId(admissionMaster.getAdmissionId())
//                .stream()
//                .map(feePayment -> new FeePaymentDTO(feePayment.getInstallmentNo().toString(),
//                        feePayment.getAmount().toString(),
//                        feePayment.getPaymentMode(),
//                        feePayment.getPaymentDate().toString().substring(0,11)))
//                .toList();
//
//        invoiceDTO.setAllInstallments(feePaymentDTOS);
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("invoice", invoiceDTO);
//
//        byte[] pdfBytes = pdfService.generatePdf("invoice", data);
//
//        sendEmailWithAttachment(admissionMaster.getUserMaster().getEmail(), "Your Fee Payment Invoice",
//                "Dear Student,<br><br>Thank you for your payment.<br>Please find your invoice attached.<br><br>Regards,<br>Xansa IT Solutions",
//                pdfBytes);
//
////        return ResponseEntity.ok()
////                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Invoice-" + "Download Invoice" + ".pdf")
////                .contentType(MediaType.APPLICATION_PDF)
////                .body(pdfBytes);
//    }
//
//
//
//    // === Email method ===
//    private void sendEmailWithAttachment(String to, String subject, String htmlBody, byte[] pdfBytes) {
//        try {
//
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(htmlBody, true);
//            helper.setFrom("contactjobsagar@gmail.com");
//
//            // Attach PDF bytes (no need to save file)
//            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
//            helper.addAttachment("FeeInvoice.pdf", resource);
//
//            mailSender.send(message);
//            System.out.println("Email sent successfully with invoice attachment!");
//        }catch(MessagingException e){
//            System.out.println("Error is "+e);
//        }
//    }


}