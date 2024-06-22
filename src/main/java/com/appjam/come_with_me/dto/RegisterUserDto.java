package com.appjam.come_with_me.dto;

import com.appjam.come_with_me.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {
    private String username;
    private int age;
    private Gender gender;
    private List<Map<String, String>> interests;
    private List<Map<String, String>> targets;
}
