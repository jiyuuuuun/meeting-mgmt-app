package com.example.meetingmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @Column(name = "schdule_time", nullable = false)
    private LocalTime scheduleTime;

    @Column(name = "schedule_location", nullable = false, length = 50)
    private String scheduleLocation;

    @JsonIgnore
    @OneToMany(mappedBy = "schedule",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleParticipant> participants = new ArrayList<>();

}
