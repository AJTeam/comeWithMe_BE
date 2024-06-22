package com.appjam.come_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRoomDto {
    private String name;
    private List<Map<String, String>> interests;
    private List<Map<String, String>> targets;

}
