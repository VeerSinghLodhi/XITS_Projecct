package com.example.SamvaadProject.emailservicespackage;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {

    // Store OTP and creation time
    private final Map<String, OtpDetails> otpStorage = new HashMap<>();

    // OTP valid for 5 minutes
    private static final int OTP_EXPIRY_MINUTES = 5;

    // Inner class to store OTP details
    private static class OtpDetails {
        String otp;
        LocalDateTime creationTime;

        OtpDetails(String otp, LocalDateTime creationTime) {
            this.otp = otp;
            this.creationTime = creationTime;
        }
    }

    // Generate new OTP and store with timestamp
    public String generateOtp(String email) {
        String otp = String.valueOf((int) ((Math.random() * 900000) + 100000)); // 6-digit OTP
        otpStorage.put(email, new OtpDetails(otp, LocalDateTime.now()));
        return otp;
    }

    // Validate OTP and check expiry
    public boolean validateOtp(String email, String enteredOtp) {
        OtpDetails details = otpStorage.get(email);

        if (details == null) {
            return false;
        }

        // Check if expired
        LocalDateTime now = LocalDateTime.now();
        if (details.creationTime.plusMinutes(OTP_EXPIRY_MINUTES).isBefore(now)) {
            otpStorage.remove(email); // remove expired OTP
            return false;
        }

        // Check if OTP matches
        boolean isValid = details.otp.equals(enteredOtp);
        if (isValid) {
            otpStorage.remove(email); // OTP used → remove it
        }

        return isValid;
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }

    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        otpStorage.entrySet().removeIf(entry ->
                entry.getValue().creationTime.plusMinutes(OTP_EXPIRY_MINUTES).isBefore(now)
        );
    }

}
