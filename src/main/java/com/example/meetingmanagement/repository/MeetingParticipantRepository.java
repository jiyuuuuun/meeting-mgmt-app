package com.example.meetingmanagement.repository;

import com.example.meetingmanagement.entity.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
    @Override
    Optional<MeetingParticipant> findById(Long aLong);
}
