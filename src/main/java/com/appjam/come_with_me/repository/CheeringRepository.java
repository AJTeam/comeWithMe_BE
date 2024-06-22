package com.appjam.come_with_me.repository;

import com.appjam.come_with_me.domain.Cheering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CheeringRepository extends JpaRepository<Cheering, Long> {
    Page<Cheering> getCheeringsByRoomId(UUID roomId, Pageable pageable);
}
