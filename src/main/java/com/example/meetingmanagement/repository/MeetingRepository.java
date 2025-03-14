package com.example.meetingmanagement.repository;

import com.example.meetingmanagement.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Override
    Optional<Meeting> findById(Long aLong);
}
