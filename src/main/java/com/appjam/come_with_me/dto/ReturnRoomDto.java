package com.appjam.come_with_me.dto;

import com.appjam.come_with_me.domain.Interest;
import com.appjam.come_with_me.domain.Room;
import com.appjam.come_with_me.domain.Target;
import com.appjam.come_with_me.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnRoomDto {
    private UUID roomId;
    private String roomName;
    private String roomTitle;
    private List<String> interests;
    private List<String> targets;
    private List<String> members;
    private String adminUser;

    public ReturnRoomDto(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getName();
        this.roomTitle = room.getTitle();
        this.interests = room.getInterests().stream().map(Interest::getName).toList();
        this.targets = room.getTargets().stream().map(Target::getName).toList();
        this.members = room.getUsers().stream().map(User::getNickname).toList();
        this.adminUser = room.getAdminUser().getNickname();
    }
}
