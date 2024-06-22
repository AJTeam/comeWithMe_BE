package com.appjam.come_with_me.service;

import com.appjam.come_with_me.common.SecurityUtils;
import com.appjam.come_with_me.domain.Mission;
import com.appjam.come_with_me.domain.User;
import com.appjam.come_with_me.dto.CreateMissionDto;
import com.appjam.come_with_me.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {
    private final MissionRepository missionRepository;
    private final UserService userService;

    public Mission createMission(CreateMissionDto createMissionDto) {
        User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());
        Mission mission = Mission.builder()
                .title(createMissionDto.getTitle())
                .user(user)
                .build();

        missionRepository.save(mission);

        user.addMission(mission);

        return mission;
    }

    public Mission getById(Long missionId) {
        return missionRepository.findById(missionId).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 미션입니다.")
        );
    }

    public Mission shardMission(Long missionId) {
        Mission mission = getById(missionId);
        User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());

        if(!mission.getUser().getId().equals(user.getId()))
            throw new IllegalStateException("사용자 소유의 미션이 아닙니다.");

        if (user.getRoom() == null)
            throw new IllegalStateException("사용자가 참여한 방이 없습니다.");
        user.getRoom().addMissions(mission);
        mission.setRoom(user.getRoom());

        return missionRepository.save(mission);
    }

    public Mission changeMissionStatus(Long missionId) {
        Mission mission = getById(missionId);
        User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());

        if(!mission.getUser().getId().equals(user.getId()))
            throw new IllegalStateException("사용자 소유의 미션이 아닙니다.");

        mission.changeSuccess();

        return missionRepository.save(mission);
    }
}
