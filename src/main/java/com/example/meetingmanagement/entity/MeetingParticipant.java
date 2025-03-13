package com.example.meetingmanagement.entity;

import jakarta.persistence.*;

@Entity
public class MeetingParticipant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingPartId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;
}
