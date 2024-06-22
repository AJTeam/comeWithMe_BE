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
    private String nickname;
    private int age;
    private Gender gender;
    private Role role;
    private String email;
    private List<Target> targets;
    private List<Interest> interests;

    public ReturnUserDto(User user) {
        this.nickname = user.getNickname();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.targets = user.getTargets();
        this.interests = user.getInterests();
    }
}
