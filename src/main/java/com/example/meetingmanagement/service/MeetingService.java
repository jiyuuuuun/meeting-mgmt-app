package com.example.meetingmanagement.service;

import com.example.meetingmanagement.dto.UserResponseDTO;
import com.example.meetingmanagement.entity.Meeting;
import com.example.meetingmanagement.entity.MeetingParticipant;
import com.example.meetingmanagement.entity.Schedule;
import com.example.meetingmanagement.entity.User;
import com.example.meetingmanagement.repository.MeetingParticipantRepository;
import com.example.meetingmanagement.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;

    //미팅 생성
    @Transactional
    public void createMeeting(Meeting meeting,User user) {
        meeting.setCreator(user);
        MeetingParticipant meetingParticipant = new MeetingParticipant(user, meeting);
        meeting.getParticipants().add(meetingParticipant);
        meetingRepository.save(meeting);
        log.info("미팅 생성 완료:: "+meeting.toString());

    }

    //모든 미팅 조회
    @Transactional(readOnly = true)
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    //미팅 수정
    @Transactional
    public boolean updateMeeting(Meeting newMeeting, User user,Long meetingId) {
        Meeting Meeting=meetingRepository.findById(meetingId).orElse(null);
        if (!Meeting.getCreator().equals(user)) {
            log.info("미팅의 생성자만 미팅을 수정할 수 있습니다.");
            return false;
        }

        Meeting.setMeetingDescription(newMeeting.getMeetingDescription());
        Meeting.setMeetingName(newMeeting.getMeetingName());
        Meeting.setMaxParticipants(newMeeting.getMaxParticipants());
        log.info(Meeting.toString());
        return true;
    }
    //미팅 삭제
    @Transactional
    public boolean deleteMeeting(Meeting meeting, User user) {
        if (!meeting.getCreator().equals(user)) {
            log.info("미팅의 생성자만 미팅을 삭제할 수 있습니다.");
            return false;
        }

        // 참가자 정보 먼저 확실하게 삭제
        meeting.getParticipants().clear();

        // 미팅 삭제
        meetingRepository.delete(meeting);

        return true;
    }
    //미팅 참가
    @Transactional
    public boolean joinMeeting(Meeting meeting, User user)  {
        // 이미 참가한 사용자인지 확인
        boolean isAlreadyJoined = meeting.getParticipants().stream()
                .anyMatch(p -> p.getUser().equals(user));
        if (isAlreadyJoined) {
            log.info("이미 참가한 미팅입니다.");
            return false;
        }

        //참가자 수 제한 체크
        if (meeting.getParticipants().size() >= 10) {
            log.info("미팅이 정원 초과되었습니다.");
            return false;
        }

        //참가자 추가
        MeetingParticipant meetingParticipant = new MeetingParticipant(user, meeting);
        meeting.getParticipants().add(meetingParticipant);

        meetingRepository.save(meeting);

        return true;
    }
    //미팅 참가자 목록 조회
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getMeetingUser(Meeting meeting) {
        Meeting meeting1=meetingRepository.findById(meeting.getMeetingId()).orElseThrow();
        List<UserResponseDTO> users=new ArrayList<>();
        meeting1.getParticipants().forEach(p->{
            users.add(UserResponseDTO.builder().name(p.getUser().getUserName()).id(p.getUser().getUserId()).build());
        });

        return users;
    }
    @Transactional(readOnly = true)
    public Meeting findMeeting(Long id) {
        if(id ==null)
            return null;
        return meetingRepository.findById(id).orElseThrow();
    }


}
