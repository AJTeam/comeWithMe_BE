package com.appjam.come_with_me.service;

import com.appjam.come_with_me.common.SecurityUtils;
import com.appjam.come_with_me.domain.Room;
import com.appjam.come_with_me.domain.RoomWaiting;
import com.appjam.come_with_me.domain.User;
import com.appjam.come_with_me.repository.RoomWaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomWaitingService {

    private final RoomWaitingRepository roomWaitingRepository;
    private final RoomService roomService;
    private final UserService userService;
    public List<RoomWaiting> getRoomWaitingByRoomId(UUID roomId) {
        return roomWaitingRepository.getRoomWaitingsByRoomId(roomId);
    }

    public void joinRequest(String roomId) {
        Room room = roomService.getRoomById(UUID.fromString(roomId));
        User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());
        RoomWaiting roomWaiting = RoomWaiting.builder()
                .room(room)
                .user(user)
                .build();

        roomWaitingRepository.save(roomWaiting);
    }

    public void acceptRoom(Long waitingId) {
        RoomWaiting roomWaiting = roomWaitingRepository.findById(waitingId).orElseThrow(
                () -> new IllegalStateException("대기자를 찾을수 없습니다.")
        );

        UUID id = SecurityUtils.getLoginUser().getId();
        Room room = roomWaiting.getRoom();

        if (!room.getAdminUser().getId().equals(id))
            throw new IllegalStateException("해당 방의 관리자가 아닙니다.");

        room.getUsers().add(roomWaiting.getUser());
        roomWaitingRepository.delete(roomWaiting);
    }

    public void cancelWaiting(Long waitingId) {
        RoomWaiting roomWaiting = roomWaitingRepository.findById(waitingId).orElseThrow(
                () -> new IllegalStateException("대기자를 찾을수 없습니다.")
        );

        UUID id = SecurityUtils.getLoginUser().getId();
        Room room = roomWaiting.getRoom();

        if (!roomWaiting.getUser().getId().equals(id) || room.getAdminUser().getId().equals(id))
            throw new IllegalStateException("이것을 실행할 권한이 없습니다.");

        roomWaitingRepository.delete(roomWaiting);

    }
}
