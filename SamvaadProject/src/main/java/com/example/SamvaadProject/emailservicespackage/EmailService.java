package com.example.SamvaadProject.emailservicespackage;


import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.feespackage.FeeRepository;
import com.example.SamvaadProject.pdfgeneratorpackage.FeePaymentDTO;
import com.example.SamvaadProject.pdfgeneratorpackage.InvoiceDTO;
import com.example.SamvaadProject.pdfgeneratorpackage.PdfService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    public void getSendUsernameAndPassword(String fullName,String username,String password,String to){
        try {

            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setTo(to);
            helper.setSubject("Welcome "+fullName);

            String logoUrl = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg0GupVn_X0cW7eVWzGcnkhdmkA5l3QN1wUaWEX3Bj_cU-33UQjI1jJlHVKLkRTsd9zQVWG2rlzHrP7PyWxXkBoQmGcfeM8yBRhaI935Kupo-5Sfy1cSjpCPXUEffEvLRb4_Jf6v6Adu4w1l7QPBuP66hFl_Ei2NaE1AENV69mU-Jv6D02K-JG3hEaYNHKl/s1650/xansaitsulutionsLogo.png";
            String companyName = "Xansa IT Solutions";

            String htmlBody = "<!DOCTYPE html>" +
                    "<html lang='en'>" +
                    "<head>" +
                    "  <meta charset='UTF-8'>" +
                    "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "  <title>Login Credentials - " + companyName + "</title>" +
                    "  <style>" +
                    "    * { margin: 0; padding: 0; box-sizing: border-box; }" +
                    "    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;" +
                    "           background-color: #f5f7fa; margin: 0; padding: 40px 20px; }" +
                    "    .email-wrapper { max-width: 600px; margin: 0 auto; background-color: #ffffff;" +
                    "                     border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.07); overflow: hidden; }" +
                    "    .logo-section { text-align: center; padding: 40px 40px 20px; background-color: #ffffff; }" +
                    "    .logo-section img { width: 140px; height: auto; margin-bottom: 16px; }" +
                    "    .logo-section h1 { font-size: 24px; font-weight: 600; color: #1a202c; margin-bottom: 8px; }" +
                    "    .divider { height: 1px; background: linear-gradient(to right, transparent, #e2e8f0, transparent);" +
                    "               margin: 0 40px; }" +
                    "    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);" +
                    "              color: #ffffff; padding: 30px 40px; text-align: center; }" +
                    "    .header h2 { font-size: 22px; font-weight: 600; margin: 0; }" +
                    "    .content { padding: 40px 40px 30px; }" +
                    "    .content > p { font-size: 16px; color: #4a5568; line-height: 1.6; margin-bottom: 24px; text-align: center; }" +
                    "    .credentials-box { background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);" +
                    "                       border: 2px solid #e2e8f0; border-radius: 10px; padding: 30px; margin: 24px 0; }" +
                    "    .credential-item { margin: 20px 0; padding: 16px; background-color: #ffffff;" +
                    "                       border-radius: 8px; border-left: 4px solid #667eea; }" +
                    "    .credential-label { font-size: 13px; font-weight: 600; color: #718096;" +
                    "                        text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 8px; }" +
                    "    .credential-value { font-size: 20px; font-weight: 700; color: #2d3748;" +
                    "                        font-family: 'Courier New', monospace; letter-spacing: 1px; word-break: break-all; }" +
                    "    .warning-box { background-color: #fef5e7; border: 1px solid #f9e79f; border-radius: 8px;" +
                    "                   padding: 16px 20px; margin: 24px 0; }" +
                    "    .warning-box p { font-size: 14px; color: #856404; line-height: 1.5; margin: 0; }" +
                    "    .security-tips { background-color: #e8f4f8; border-radius: 8px; padding: 20px; margin: 24px 0; }" +
                    "    .security-tips h3 { font-size: 15px; color: #1a202c; margin-bottom: 12px; font-weight: 600; }" +
                    "    .security-tips ul { margin: 0; padding-left: 20px; }" +
                    "    .security-tips li { font-size: 13px; color: #4a5568; line-height: 1.6; margin: 6px 0; }" +
                    "    .footer { background-color: #f7fafc; padding: 30px 40px; text-align: center; border-top: 1px solid #e2e8f0; }" +
                    "    .footer p { font-size: 13px; color: #a0aec0; margin: 4px 0; line-height: 1.5; }" +
                    "    .footer-note { font-size: 12px; color: #cbd5e0; margin-top: 12px; }" +
                    "  </style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class='email-wrapper'>" +
                    "    <div class='logo-section'>" +
                    "      <img src='" + logoUrl + "' alt='" + companyName + " Logo'>" +
                    "      <h1>" + companyName + "</h1>" +
                    "    </div>" +
                    "    <div class='divider'></div>" +
                    "    <div class='header'>" +
                    "      <h2>🔐 Your Account Credentials</h2>" +
                    "    </div>" +
                    "    <div class='content'>" +
                    "      <p>Welcome! Your account has been successfully created. Below are your login credentials:</p>" +
                    "      <div class='credentials-box'>" +
                    "        <div class='credential-item'>" +
                    "          <div class='credential-label'>Username</div>" +
                    "          <div class='credential-value'>" + username + "</div>" +
                    "        </div>" +
                    "        <div class='credential-item'>" +
                    "          <div class='credential-label'>Password</div>" +
                    "          <div class='credential-value'>" + password + "</div>" +
                    "        </div>" +
                    "      </div>" +
                    "      <div class='warning-box'>" +
                    "        <p><strong>⚠️ Important:</strong> For your security, please change your password after your first login.</p>" +
                    "      </div>" +
                    "      <div class='security-tips'>" +
                    "        <h3>🛡️ Security Best Practices</h3>" +
                    "        <ul>" +
                    "          <li>Never share your credentials with anyone</li>" +
                    "          <li>Use a strong, unique password for your account</li>" +
                    "          <li>Keep this email secure or delete it after saving your credentials</li>" +
                    "        </ul>" +
                    "      </div>" +
                    "    </div>" +
                    "    <div class='footer'>" +
                    "      <p>© "+ LocalDate.now().getYear() + " " + companyName + ". All rights reserved.</p>" +
                    "      <p>Secure Account Management System</p>" +
                    "      <p class='footer-note'>This is an automated message. Please do not reply to this email.</p>" +
                    "    </div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";


            helper.setText(htmlBody,true);
            mailSender.send(message);


        }catch(Exception e){
            System.out.println("Error is "+e);
        }
    }

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("🔒 OTP Verification - Secure Password Update");

            // Replace this with your logo URL (it can be hosted on your server or an image CDN)
            String logoUrl = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg0GupVn_X0cW7eVWzGcnkhdmkA5l3QN1wUaWEX3Bj_cU-33UQjI1jJlHVKLkRTsd9zQVWG2rlzHrP7PyWxXkBoQmGcfeM8yBRhaI935Kupo-5Sfy1cSjpCPXUEffEvLRb4_Jf6v6Adu4w1l7QPBuP66hFl_Ei2NaE1AENV69mU-Jv6D02K-JG3hEaYNHKl/s1650/xansaitsulutionsLogo.png";
            String appName = "Xansa IT Solutions"; // you can change this to your app name


            String htmlContent = """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        </head>
                        <body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; background-color: #f5f7fa;">
                            <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #f5f7fa;">
                                <tr>
                                    <td align="center" style="padding: 40px 20px;">
                                        <table role="presentation" width="600" cellspacing="0" cellpadding="0" border="0" style="background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.07); max-width: 600px; width: 100%%;">
                    
                                            <!-- Header with Logo -->
                                            <tr>
                                                <td align="center" style="padding: 40px 40px 30px;">
                                                    <img src="%s" alt="%s" style="width: 140px; height: auto; display: block; margin-bottom: 16px;">
                                                    <h1 style="margin: 0; font-size: 24px; font-weight: 600; color: #1a202c;">%s</h1>
                                                </td>
                                            </tr>
                    
                                            <!-- Divider -->
                                            <tr>
                                                <td style="padding: 0 40px;">
                                                    <div style="height: 1px; background: linear-gradient(to right, transparent, #e2e8f0, transparent);"></div>
                                                </td>
                                            </tr>
                    
                                            <!-- Main Content -->
                                            <tr>
                                                <td style="padding: 40px 40px 30px;">
                                                    <h2 style="margin: 0 0 20px; font-size: 20px; font-weight: 600; color: #2d3748;">Password Update Verification</h2>
                                                    <p style="margin: 0 0 24px; font-size: 16px; line-height: 1.6; color: #4a5568;">
                                                        We received a request to update your password. To proceed securely, please use the verification code below:
                                                    </p>
                    
                                                    <!-- OTP Box -->
                                                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0">
                                                        <tr>
                                                            <td align="center" style="padding: 24px 0;">
                                                                <table role="presentation" cellspacing="0" cellpadding="0" border="0" style="background: #FFC451; border-radius: 8px;">
                                                                    <tr>
                                                                        <td style="padding: 20px 40px;">
                                                                            <div style="font-size: 36px; font-weight: 700; letter-spacing: 8px; color: #000; font-family: 'Courier New', monospace;">%s</div>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                    
                                                    <!-- Info Box -->
                                                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #edf2f7; border-radius: 8px; margin: 24px 0;">
                                                        <tr>
                                                            <td style="padding: 16px 20px;">
                                                                <p style="margin: 0; font-size: 14px; line-height: 1.5; color: #2d3748;">
                                                                    <strong style="color: #1a202c;">⏱️ Valid for 5 minutes</strong><br>
                                                                    For your security, never share this code with anyone, including our support team.
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                    
                                                    <p style="margin: 24px 0 0; font-size: 14px; line-height: 1.6; color: #718096;">
                                                        If you didn't request this password change, please ignore this email or contact our support team if you have concerns about your account security.
                                                    </p>
                                                </td>
                                            </tr>
                    
                                            <!-- Footer -->
                                            <tr>
                                                <td style="padding: 0 40px 40px;">
                                                    <div style="height: 1px; background: linear-gradient(to right, transparent, #e2e8f0, transparent); margin-bottom: 24px;"></div>
                                                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0">
                                                        <tr>
                                                            <td align="center">
                                                                <p style="margin: 0 0 8px; font-size: 13px; color: #a0aec0;">
                                                                    © %s %s. All rights reserved.
                                                                </p>
                                                                <p style="margin: 0; font-size: 12px; color: #cbd5e0;">
                                                                    Secure Account Management System
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                    
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </body>
                        </html>
                        """.formatted(logoUrl, appName, appName, otp, LocalDate.now().getYear(), appName);


            helper.setText(htmlContent, true); // true = HTML content
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    @Autowired
    AdmissionRepository admissionRepository;

    @Autowired
    FeeRepository feeRepository;

    @Autowired
    PdfService pdfService;

    //    @GetMapping("/download/invoiceId")
//    public ResponseEntity<byte[]> downloadInvoice() throws IOException {
    public void getSendInvoice(String admissionId)  {
        try {
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            AdmissionMaster admissionMaster = admissionRepository.findById(admissionId).orElse(null);

            invoiceDTO.setStudentName(admissionMaster.getUserMaster().getFullName());
            invoiceDTO.setAdmissionId(admissionMaster.getAdmissionId());
            invoiceDTO.setCourseName(admissionMaster.getCourse().getCourseName());
            invoiceDTO.setFeeCategory(admissionMaster.getFeeCategory());

            invoiceDTO.setCourseFee(admissionMaster.getCourseFee().toString());
            invoiceDTO.setDiscount(admissionMaster.getDiscount().toString());
            invoiceDTO.setNetPayableAmount(admissionMaster.getNetFees().toString());


            if(!admissionMaster.getFeeCategory().equals("Lumpsum")){
                invoiceDTO.setRegistrationFee(admissionMaster.getRegistrationFee().toString());
                invoiceDTO.setTotalInstallments(admissionMaster.getNoOfInstallments().toString());
                List<FeePaymentDTO> feePaymentDTOS = feeRepository.findByAdmission_AdmissionId(admissionMaster.getAdmissionId())
                        .stream()
                        .map(feePayment -> new FeePaymentDTO(feePayment.getInstallmentNo().toString(),
                                feePayment.getAmount().toString(),
                                feePayment.getPaymentMode(),
                                feePayment.getPaymentDate().toString(),feePayment.getBalanceAfterPayment().toString()))
                        .toList();

                invoiceDTO.setPendingBalance(feePaymentDTOS.get(feePaymentDTOS.size()-1).getPendingAmount());
                invoiceDTO.setAllInstallments(feePaymentDTOS);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("invoice", invoiceDTO);

            byte[] pdfBytes = pdfService.generatePdf("invoice", data);

            sendEmailWithAttachment(admissionMaster.getUserMaster().getEmail(), "Your Fee Payment Invoice",
                    "Dear Student,<br><br>Thank you for your payment.<br>Please find your invoice attached.<br><br>Regards,<br>Xansa IT Solutions",
                    pdfBytes);
        }catch(Exception e){
            System.out.println("Error is "+e);
        }
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Invoice-" + "Download Invoice" + ".pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(pdfBytes);
    }



    // === Email method ===
    private void sendEmailWithAttachment(String to, String subject, String htmlBody, byte[] pdfBytes) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom("contactjobsagar@gmail.com");

            // Attach PDF bytes (no need to save file)
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            helper.addAttachment("FeeInvoice.pdf", resource);

            mailSender.send(message);
            System.out.println("Email sent successfully with invoice attachment!");
        }catch(MessagingException e){
            System.out.println("Error is "+e);
        }
    }

}
