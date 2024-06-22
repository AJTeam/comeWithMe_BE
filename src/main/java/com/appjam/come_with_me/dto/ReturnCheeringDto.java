package com.appjam.come_with_me.dto;

import com.appjam.come_with_me.domain.Cheering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnCheeringDto {
    private String comment;
    private String nickname;

    public ReturnCheeringDto(Cheering cheering) {
        this.comment = cheering.getComment();
        this.nickname = cheering.getUser().getNickname();
    }
}
