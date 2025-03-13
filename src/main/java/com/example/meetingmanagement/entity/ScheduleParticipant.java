package com.example.meetingmanagement.entity;

import jakarta.persistence.*;

@Entity
public class ScheduleParticipant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedulePartId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.DEFAULT;

    public enum Status {
        ATTENDING, MAYBE, NOT_ATTENDING, DEFAULT
    }
}
