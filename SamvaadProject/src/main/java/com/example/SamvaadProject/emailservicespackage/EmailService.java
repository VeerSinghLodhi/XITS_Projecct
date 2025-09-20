package com.example.SamvaadProject.emailservicespackage;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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

            String htmlBody = "<!DOCTYPE html>" +
                    "<html lang='en'>" +
                    "<head>" +
                    "  <meta charset='UTF-8'>" +
                    "  <title>Login Credentials</title>" +
                    "  <style>" +
                    "    body { font-family: 'Segoe UI', Tahoma, sans-serif; background-color: #f4f6f9; margin: 0; padding: 40px 0; }" +
                    "    .container { max-width: 500px; margin: auto; background-color: #ffffff; border-radius: 10px;" +
                    "                box-shadow: 0 8px 20px rgba(0,0,0,0.1); overflow: hidden; }" +
                    "    .header { background: linear-gradient(135deg, #0077cc, #00aaff); color: #fff; padding: 20px; text-align: center; }" +
                    "    .header h2 { margin: 0; font-size: 22px; font-weight: 600; }" +
                    "    .content { padding: 30px; text-align: center; }" +
                    "    .credentials { background:#f9fbfd; border:1px solid #e1e6ed; padding:20px; border-radius:8px; text-align:left; }" +
                    "    .credentials p { font-size: 16px; margin: 10px 0; }" +
                    "    .credentials span { font-weight: bold; color: #0077cc; }" +
                    "    .footer { background:#f9f9f9; text-align:center; padding:15px; font-size:12px; color:#777; }" +
                    "  </style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class='container'>" +
                    "    <div class='header'><h2>Your Login Credentials</h2></div>" +
                    "    <div class='content'>" +
                    "      <p>Below are your account details:</p>" +
                    "      <div class='credentials'>" +
                    "        <p><span>Username:</span> <h1>" + username + "</h1></p>" +
                    "        <p><span>Password:</span> <h1>" + password + "</h1></p>" +
                    "      </div>" +
                    "    </div>" +
                    "    <div class='footer'>Please keep your credentials safe and do not share them.</div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";


            helper.setText(htmlBody,true);
            mailSender.send(message);


        }catch(Exception e){
            System.out.println("Error is "+e);
        }
    }
}
