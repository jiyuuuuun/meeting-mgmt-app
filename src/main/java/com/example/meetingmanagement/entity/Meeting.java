package com.example.meetingmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
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
