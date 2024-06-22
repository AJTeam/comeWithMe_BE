package com.appjam.come_with_me.dto;

import com.appjam.come_with_me.domain.RoomWaiting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnRoomWaitingDto {
    private ReturnRoomDto room;
    private ReturnUserDto user;

    public ReturnRoomWaitingDto(RoomWaiting roomWaiting) {
        room = new ReturnRoomDto(roomWaiting.getRoom());
        user = new ReturnUserDto(roomWaiting.getUser());
    }
}
