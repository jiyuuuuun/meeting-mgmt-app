package com.example.meetingmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 엔티티")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    @Schema(description = "사용자 ID", example = "1")
    private long userId;

    @Column(name = "user_email", unique = true, nullable = false)
    @Schema(description = "이메일", example = "test@example.com")
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    @Schema(description = "비밀번호 (암호화 저장)", example = "encrypted_password")
    private String userPassword;

    @Column(name = "user_name")
    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "계정 생성일", example = "2025-03-14T09:18:12.608877")
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    @Schema(description = "사용자가 생성한 모임 리스트")
    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "사용자의 역할 리스트")
    private List<UserRole> userRoles = new ArrayList<>();
}
