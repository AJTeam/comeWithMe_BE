package com.appjam.come_with_me.service;

import com.appjam.come_with_me.common.SecurityUtils;
import com.appjam.come_with_me.domain.Interest;
import com.appjam.come_with_me.domain.Room;
import com.appjam.come_with_me.domain.Target;
import com.appjam.come_with_me.domain.User;
import com.appjam.come_with_me.dto.CreateRoomDto;
import com.appjam.come_with_me.repository.RoomRepository;
import com.appjam.come_with_me.repository.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final TargetService targetService;
    private final InterestService interestService;
    private final UserService userService;

    public Room createRoom(CreateRoomDto createRoomDto) {

        List<Target> targets = new ArrayList<>();
        List<Interest> interests = new ArrayList<>();

        for (Map<String, String> targetName : createRoomDto.getTargets()) {
            Target target = targetService.getTargetByName(targetName.get("name"));
            if(target == null) {
                target = targetService.saveTarget(targetName.get("name"));
            }
            targets.add(target);
        }

        for (Map<String, String> interestName : createRoomDto.getInterests()) {
            Interest interest = interestService.getInterestByName(interestName.get("name"));
            if (interest == null) {
                interest = interestService.saveInterest(interestName.get("name"));
            }
            interests.add(interest);
        }

        User user = SecurityUtils.getLoginUser();

        user = userService.getUserByUserToken(user.getToken());

        Room room = Room.builder()
                .name(createRoomDto.getName())
                .interests(interests.isEmpty() ? null : interests)
                .targets(targets.isEmpty() ? null : targets)
                .users(Collections.singletonList(user))
                .build();
        return roomRepository.save(room);
    }

}
