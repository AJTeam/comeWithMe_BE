package com.appjam.come_with_me.service;

import com.appjam.come_with_me.common.SecurityUtils;
import com.appjam.come_with_me.domain.Feedback;
import com.appjam.come_with_me.domain.User;
import com.appjam.come_with_me.dto.CreateFeedbackDto;
import com.appjam.come_with_me.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;

    public Feedback createFeedback(CreateFeedbackDto createFeedBackDto) {
        User toUser = userService.getByUserId(createFeedBackDto.getToUserId());
        Feedback feedback = Feedback.builder()
                .toUser(toUser)
                .message(createFeedBackDto.getMessage())
                .room(toUser.getRoom())
                .sendUser(userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken()))
                .build();

        return feedbackRepository.save(feedback);
    }

    public Feedback getById(Long id) {
        return feedbackRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 피드백 입니다.")
        );
    }
    public Feedback editFeedback(Long feedbackId, String newMessage) {
        Feedback feedback = getById(feedbackId);
        feedback.setMessage(newMessage);

        return feedbackRepository.save(feedback);
    }

    public Page<Feedback> getFeedbacksByRoom(Pageable pageable) {
        User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());
        Page<Feedback> feedbacks = feedbackRepository.getFeedbacksByRoomId(user.getRoom().getId(), pageable);

        return feedbacks;
    }
}
