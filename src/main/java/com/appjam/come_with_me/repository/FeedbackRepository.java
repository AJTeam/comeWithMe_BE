package com.appjam.come_with_me.repository;

import com.appjam.come_with_me.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> getFeedbacksByRoomId(UUID roomId, Pageable pageable);
}
