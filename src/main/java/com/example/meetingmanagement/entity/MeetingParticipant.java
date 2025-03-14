package com.example.meetingmanagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter@Setter
@NoArgsConstructor
@Schema(description = "모임 참가 엔티티")
public class MeetingParticipant {
    public MeetingParticipant(User user, Meeting meeting) {
        this.user = user;
        this.meeting = meeting;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "모임 참가 ID", example = "1")
    private Long meetingPartId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "참가자 정보")
    private User user;

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    @Schema(description = "참여한 모임 정보")
    private Meeting meeting;
}
