package com.example.meetingmanagement.repository;

import com.example.meetingmanagement.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
