package com.example.meetingmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meetings")
@Getter@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "모임 엔티티")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    @Schema(description = "모임 ID", example = "1")
    private Long meetingId;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    @Schema(description = "모임 생성자 정보")
    private User creator;

    @Column(name="meeting_name", nullable = false)
    @Schema(description = "모임 이름", example = "스프링 스터디")
    private String meetingName;

    @Column(name="meeting_description", nullable = false)
    @Schema(description = "모임 설명", example = "매주 월요일 7시에 진행되는 스터디")
    private String meetingDescription;

    @Column(nullable = false)
    @Schema(description = "최대 참가 가능 인원", example = "10")
    private int maxParticipants = 10;

    @OneToMany(mappedBy = "meeting")
    @Schema(description = "모임 일정 리스트")
    private List<Schedule> schedules = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "모임 참가자 리스트")
    private List<MeetingParticipant> participants = new ArrayList<>();


    @Override
    public String toString() {
        return "Meeting{" +
                "meetingId=" + meetingId +
                ", creator=" + creator +
                ", meetingName='" + meetingName + '\'' +
                ", meetingDescription='" + meetingDescription + '\'' +
                '}';
    }
}
