package com.example.meetingmanagement.controller;

import com.example.meetingmanagement.dto.ErrorResponseDTO;
import com.example.meetingmanagement.dto.LoginResponseDTO;
import com.example.meetingmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication API", description = "사용자 인증 관련 API")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "사용자를 등록합니다.")
    public ResponseEntity<String> register(
            @RequestBody @Parameter(description = "회원가입 정보") Map<String, String> user) {
        return ResponseEntity.ok(userService.register(user.get("email"), user.get("password"), user.get("username")));
    }

    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "사용자가 로그인하고 JWT 토큰을 받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
            }
    )
    public ResponseEntity<?> login(@RequestBody @Parameter(description = "로그인 정보") Map<String, String> user) {
        String token = userService.login(user.get("email"), user.get("password"));
        return token != null ? ResponseEntity.ok(new LoginResponseDTO(token)) : ResponseEntity.status(401).body(new ErrorResponseDTO("Invalid credentials"));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자가 로그아웃합니다.")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        userService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }
}