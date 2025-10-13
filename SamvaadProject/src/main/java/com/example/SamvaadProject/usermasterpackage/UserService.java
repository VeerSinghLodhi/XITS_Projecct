package com.example.SamvaadProject.usermasterpackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserMaster login(String username, String password) {
        Optional<UserMaster> user = userRepository.findByUsernameAndPassword(username, password);
        return user.orElse(null);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String updatePassword(String username, String currentPassword, String newPassword) {
//        System.out.println("Username "+username);
        UserMaster user = userRepository.findByUsername(username);
//        System.out.println("Inside the Update Password Faculty Service Method");
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "currentFalse";
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return "currentSame";
        }
        // Encode and set new password
        user.setPassword(passwordEncoder.encode(newPassword));

        // Save user
        userRepository.save(user);
        return "updated";
    }
}
