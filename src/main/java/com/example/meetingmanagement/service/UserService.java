package com.example.meetingmanagement.service;

import com.example.meetingmanagement.entity.Role;
import com.example.meetingmanagement.entity.User;
import com.example.meetingmanagement.entity.UserRole;
import com.example.meetingmanagement.repository.RoleRepository;
import com.example.meetingmanagement.repository.UserRepository;
import com.example.meetingmanagement.repository.UserRoleRepository;
import com.example.meetingmanagement.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtil jwtUtil;


    public String register(String email, String password,String username) {
        if (userRepository.findByUserEmail(email).isPresent()) {
            return "Email already exists";
        }
        Role role=roleRepository.findByRoleName("admin");


        User user = new User();
        user.setUserEmail(email);
        user.setUserPassword(password);
        user.setUserName(username);

        UserRole userRole=new UserRole(user,role);
        user.getUserRoles().add(userRole);


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

    public User getUser(String token) {
        String jwt = token.substring(7);
        Long userId=jwtUtil.validateToken(jwt);
        return userRepository.findById(userId).orElse(null);
    }
}
