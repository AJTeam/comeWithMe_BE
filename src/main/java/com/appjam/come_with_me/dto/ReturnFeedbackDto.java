package com.appjam.come_with_me.dto;

import com.appjam.come_with_me.domain.Feedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnFeedbackDto {
    private Long feedbackId;
    private ReturnUserDto sendUser;
    private ReturnUserDto toUser;

    public ReturnFeedbackDto(Feedback feedback) {
        this.feedbackId = feedback.getId();
        this.sendUser = new ReturnUserDto(feedback.getSendUser());
        this.toUser = new ReturnUserDto(feedback.getToUser());
    }
}
