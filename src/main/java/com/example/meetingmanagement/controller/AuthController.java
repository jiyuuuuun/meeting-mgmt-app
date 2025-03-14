package com.example.meetingmanagement.controller;

import com.example.meetingmanagement.dto.ErrorResponseDTO;
import com.example.meetingmanagement.dto.LoginResponseDTO;
import com.example.meetingmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
        private final UserService userService;

        @PostMapping("/register")
        public ResponseEntity<String> register(@RequestBody Map<String, String> user) {
            return ResponseEntity.ok(userService.register(user.get("email"), user.get("password"),user.get("username")));
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
            String token = userService.login(user.get("email"), user.get("password"));
            return token != null ? ResponseEntity.ok(new LoginResponseDTO(token)) : ResponseEntity.status(401).body(new ErrorResponseDTO("Invalid credentials"));
        }
        @PostMapping("/logout")
        public ResponseEntity<String> logout(HttpServletRequest request) {
            String token=request.getHeader("Authorization");
            userService.logout(token);
            return ResponseEntity.ok("Logout successful");
        }

}
