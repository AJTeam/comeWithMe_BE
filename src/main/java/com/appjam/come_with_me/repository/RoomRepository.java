package com.appjam.come_with_me.repository;

import com.appjam.come_with_me.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
}
