package com.appjam.come_with_me.service;

import com.appjam.come_with_me.common.SecurityUtils;
import com.appjam.come_with_me.domain.Interest;
import com.appjam.come_with_me.domain.Room;
import com.appjam.come_with_me.domain.Target;
import com.appjam.come_with_me.domain.User;
import com.appjam.come_with_me.dto.CreateRoomDto;
import com.appjam.come_with_me.repository.InterestRepository;
import com.appjam.come_with_me.repository.RoomRepository;
import com.appjam.come_with_me.repository.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final TargetService targetService;
    private final InterestService interestService;
    private final UserService userService;
    private final TargetRepository targetRepository;
    private final InterestRepository interestRepository;

    public Room createRoom(CreateRoomDto createRoomDto) {

        List<Target> targets = new ArrayList<>();
        List<Interest> interests = new ArrayList<>();

        for (Map<String, String> targetName : createRoomDto.getTargets()) {
            Target target = targetService.saveTarget(targetName.get("name"));
            targets.add(target);
        }

        for (Map<String, String> interestName : createRoomDto.getInterests()) {
            Interest interest = interestService.saveInterest(interestName.get("name"));
            interests.add(interest);
        }

        User user = SecurityUtils.getLoginUser();

        user = userService.getUserByUserToken(user.getToken());

        Room room = Room.builder()
                .name(createRoomDto.getName())
                .title(createRoomDto.getTitle())
                .interests(interests.isEmpty() ? null : interests)
                .targets(targets.isEmpty() ? null : targets)
                .users(Collections.singletonList(user))
                .adminUser(user)
                .build();
        roomRepository.save(room);

        for (Interest interest : interests) {
            interest.setRoom(room);
            interestService.save(interest);
        }

        for (Target target : targets) {
            target.setRoom(room);
            targetService.save(target);
        }

        user.setRoom(room);
        return room;
    }

    public Room getRoomById(UUID roomId) {
        return roomRepository.findById(roomId).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 방 입니다.")
        );
    }

    public Room joinRoom(UUID roomId) {
        Room room = getRoomById(roomId);
        User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());
        if(room.getUsers().size() >= 10)
            throw new IllegalStateException("이미 가득 찬 방입니다.");
        room.getUsers().add(user);

        return room;
    }

    public Page<Room> getRoomByPage(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }

    public void deleteRoom(UUID roomId) {
        User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());

        Room room = getRoomById(roomId);

        if(!room.getAdminUser().getId().equals(user.getId()))
            throw new IllegalStateException("해당 방의 관리자가 아닙니다.");

        interestRepository.deleteAll(room.getInterests());
        targetRepository.deleteAll(room.getTargets());

        user.setRoom(null);
        roomRepository.delete(room);
    }
}
