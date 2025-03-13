package com.example.meetingmanagement.service;

import com.example.meetingmanagement.entity.User;
import com.example.meetingmanagement.repository.UserRepository;
import com.example.meetingmanagement.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public String register(String email, String password) {
        if (userRepository.findByUserEmail(email).isPresent()) {
            return "Email already exists";
        }
        User user = new User();
        user.setUserEmail(email);
        user.setUserPassword(password);
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(String email, String password) {
        Optional<User> user = userRepository.findByUserEmail(email);
        if (user.isPresent() && password.equals(user.get().getUserPassword())) {
            return jwtUtil.generateToken(user.get().getUserId());
        }
        return null;
    }

    public void logout(String token) {
        jwtUtil.invalidateToken(token);
    }
}
