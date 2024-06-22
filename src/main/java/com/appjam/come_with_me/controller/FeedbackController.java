package com.appjam.come_with_me.controller;

import com.appjam.come_with_me.common.Result;
import com.appjam.come_with_me.domain.Feedback;
import com.appjam.come_with_me.dto.CreateFeedbackDto;
import com.appjam.come_with_me.dto.EditFeedbackDto;
import com.appjam.come_with_me.dto.ReturnFeedbackDto;
import com.appjam.come_with_me.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<?> createFeedback(@RequestBody CreateFeedbackDto createFeedbackDto) {
        Feedback feedback = feedbackService.createFeedback(createFeedbackDto);

        return ResponseEntity.ok(new Result<>(new ReturnFeedbackDto(feedback)));
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editFeedback(@RequestBody EditFeedbackDto editFeedbackDto) {
        Feedback feedback = feedbackService.editFeedback(editFeedbackDto.getFeedbackId(), editFeedbackDto.getNewMessage());

        return ResponseEntity.ok(new Result<>(new ReturnFeedbackDto(feedback)));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getByRoom(@PageableDefault Pageable pageable) {
        Page<Feedback> feedbacks = feedbackService.getFeedbacksByRoom(pageable);

        return ResponseEntity.ok(new Result<>(feedbacks.map(ReturnFeedbackDto::new).toList()));
    }
}
