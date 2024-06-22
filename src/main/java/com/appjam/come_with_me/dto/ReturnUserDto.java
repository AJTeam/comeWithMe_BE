package com.appjam.come_with_me.dto;

import com.appjam.come_with_me.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnUserDto {
    private String userId;
    private String nickname;
    private int age;
    private Gender gender;
    private Role role;
    private String email;
    private String imgUrl;
    private List<String> targets;
    private List<String> interests;
    private List<ReturnMissionDto> missions;

    public ReturnUserDto(User user) {
        this.userId = user.getId().toString();
        this.nickname = user.getNickname();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.imgUrl = user.getImg();
        this.targets = user.getTargets().stream().map(Target::getName).toList();
        this.interests = user.getInterests().stream().map(Interest::getName).toList();
        this.missions = user.getMissions().stream().map(ReturnMissionDto::new).toList();
    }
}
