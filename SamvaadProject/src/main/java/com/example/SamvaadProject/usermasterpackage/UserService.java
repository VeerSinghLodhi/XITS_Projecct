package com.example.SamvaadProject.usermasterpackage;

import org.springframework.beans.factory.annotation.Autowired;
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
}
