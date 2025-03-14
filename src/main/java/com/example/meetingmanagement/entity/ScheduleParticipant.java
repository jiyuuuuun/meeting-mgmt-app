package com.example.meetingmanagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
@Schema(description = "일정 참가 엔티티")
public class ScheduleParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "일정 참가 ID", example = "1")
    private Long schedulePartId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "참가자 정보")
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @Schema(description = "해당 참가자가 속한 일정")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "참가 상태", example = "ATTENDING")
    private Status status = Status.DEFAULT;

    public enum Status {
        ATTENDING, MAYBE, NOT_ATTENDING, DEFAULT
    }
}
