package com.example.meetingmanagement.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long meetingId;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name="meeting_name",nullable = false)
    private String meetingName;

    @Column(name="meeting_description",nullable = false)
    private String meetingDescription;

    @Column(nullable = false)
    private int maxParticipants = 10; // 기본값 10

    @OneToMany(mappedBy = "meeting")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "meeting")
    private List<MeetingParticipant> participants = new ArrayList<>();

}
