package com.example.meetingmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter@Setter
@NoArgsConstructor
public class MeetingParticipant {
    public MeetingParticipant(User user, Meeting meeting) {
        this.user = user;
        this.meeting = meeting;
    }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingPartId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;
}
