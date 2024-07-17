package com.example.GuviAssessmentLoginPage.service;

import com.example.GuviAssessmentLoginPage.entity.User;
import com.example.GuviAssessmentLoginPage.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //find the user's details based on the email id
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //Save the user details
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    //save the updated user details
    public void update(User user) {
        userRepository.save(user);
    }

    //Authenticate the user details using email and password
    public boolean authenticate(String email, String password) {
        User user = findByEmail(email);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }


}

