package com.appjam.come_with_me.repository;

import com.appjam.come_with_me.domain.RoomWaiting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomWaitingRepository extends JpaRepository<RoomWaiting, Long> {
    List<RoomWaiting> getRoomWaitingsByRoomId(UUID roomId);
}
