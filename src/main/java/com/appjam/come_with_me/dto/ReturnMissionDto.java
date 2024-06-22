package com.appjam.come_with_me.dto;

import com.appjam.come_with_me.domain.Mission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnMissionDto {
    private Long missionId;
    private String title;
    private String nickname;
    private boolean success;

    public ReturnMissionDto(Mission mission) {
        this.title = mission.getTitle();
        this.nickname = mission.getUser().getNickname();
        this.success = mission.isSuccess();
    }
}
