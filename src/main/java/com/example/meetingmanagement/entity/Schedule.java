package com.example.meetingmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@Table(name = "schedule")
@Schema(description = "모임 일정 엔티티")
@ToString
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    @Schema(description = "일정 ID", example = "1")
    private Long scheduleId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    @Schema(description = "해당 일정이 속한 모임")
    private Meeting meeting;

    @Column(nullable = false, length = 50)
    @Schema(description = "일정 제목", example = "스터디 모임")
    private String title;

    @Column(name = "schedule_date", nullable = false)
    @Schema(description = "일정 날짜", example = "2025-03-20")
    private LocalDate scheduleDate=LocalDate.now();

    @Column(name = "schedule_time", nullable = false)
    @Schema(description = "일정 시간", example = "19:00:00")
    private LocalTime scheduleTime=LocalTime.now();

    @Column(name = "schedule_location", nullable = false, length = 50)
    @Schema(description = "일정 장소", example = "강남역 스타벅스")
    private String scheduleLocation;

    @JsonIgnore
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "일정 참가자 리스트")
    private List<ScheduleParticipant> participants = new ArrayList<>();

}
