package com.appjam.come_with_me.controller;

import com.appjam.come_with_me.common.Result;
import com.appjam.come_with_me.domain.Room;
import com.appjam.come_with_me.dto.CreateRoomDto;
import com.appjam.come_with_me.dto.ReturnRoomDto;
import com.appjam.come_with_me.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomDto createRoomDto) {
        try {

            Room room = roomService.createRoom(createRoomDto);

            return ResponseEntity.ok(new Result<>(new ReturnRoomDto(room)));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getRooms(@PageableDefault Pageable pageable) {
        List<ReturnRoomDto> list = roomService.getRoomByPage(pageable).stream().map(ReturnRoomDto::new).toList();

        return ResponseEntity.ok(new Result<>(list));
    }

    @PostMapping("/join/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable("roomId") String roomId) {
        try {

            Room room = roomService.joinRoom(UUID.fromString(roomId));

            return ResponseEntity.ok(new Result<>(new ReturnRoomDto(room)));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove/{roomId}")
    public ResponseEntity<?> removeRoom(@PathVariable("roomId") String roomId) {
        try {

            roomService.deleteRoom(UUID.fromString(roomId));

            return ResponseEntity.ok(new Result<>("정상적으로 삭제되었습니다."));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
