package com.example.meetingmanagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Schema(description = "역할 엔티티")
public class Role {
    @Id
    @Column(name = "role_id")
    @Schema(description = "역할 ID", example = "1")
    private Long roleId;

    @Column(name = "role_name", nullable = false)
    @Schema(description = "역할명", example = "USER")
    private String roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "사용자 역할 리스트")
    private List<UserRole> userRoles = new ArrayList<>();
}
