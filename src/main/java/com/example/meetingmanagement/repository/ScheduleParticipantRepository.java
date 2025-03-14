package com.example.meetingmanagement.repository;

import com.example.meetingmanagement.entity.ScheduleParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleParticipantRepository extends JpaRepository<ScheduleParticipant, Long> {

    Optional<ScheduleParticipant> findBySchedulePartId(Long aLong);
}
