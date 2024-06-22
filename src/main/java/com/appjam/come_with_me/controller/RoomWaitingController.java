package com.appjam.come_with_me.controller;

import com.appjam.come_with_me.common.Result;
import com.appjam.come_with_me.domain.RoomWaiting;
import com.appjam.come_with_me.dto.ReturnRoomWaitingDto;
import com.appjam.come_with_me.service.RoomWaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/waiting")
public class RoomWaitingController {
    private final RoomWaitingService roomWaitingService;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoomWaitingsByRoomId(@PathVariable("roomId") String roomId) {
        List<RoomWaiting> roomWaitings = roomWaitingService.getRoomWaitingByRoomId(UUID.fromString(roomId));

        List<ReturnRoomWaitingDto> roomWaitingDtos = roomWaitings.stream().map(ReturnRoomWaitingDto::new).toList();
        return ResponseEntity.ok(new Result<>(roomWaitings));
    }

    @PostMapping("/join/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable("roomId") String roomId) {
        try {

            roomWaitingService.joinRequest(roomId);

            return ResponseEntity.ok(new Result<>("정상적으로 신청되었습니다."));
        }catch (IllegalStateException e) {
            return new ResponseEntity(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/accept/{waitingId}")
    public ResponseEntity<?> waitingId(@PathVariable("waitingId") Long waitingId) {
        try {

            roomWaitingService.acceptRoom(waitingId);

            return ResponseEntity.ok(new Result<>("정상적으로 수락되었습니다."));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cancel/{waitingId}")
    public ResponseEntity<?> cancelWaiting(@PathVariable("waitingId") Long waitingId) {
        try {

            roomWaitingService.cancelWaiting(waitingId);

            return ResponseEntity.ok(new Result<>("정상적으로 취소하였습니다."));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
