package com.example.SamvaadProject.emailservicespackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OtpCleanupTask {

    @Autowired
    private OtpService otpService;

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void cleanupExpiredOtps() {
        otpService.cleanupExpiredOtps();
    }
}
