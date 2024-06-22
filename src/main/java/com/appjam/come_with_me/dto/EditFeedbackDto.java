package com.appjam.come_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditFeedbackDto {
    private Long feedbackId;
    private String newMessage;
}
