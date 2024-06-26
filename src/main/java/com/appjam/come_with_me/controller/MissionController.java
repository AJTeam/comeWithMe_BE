package com.appjam.come_with_me.controller;

import com.appjam.come_with_me.common.Result;
import com.appjam.come_with_me.common.SecurityUtils;
import com.appjam.come_with_me.domain.Mission;
import com.appjam.come_with_me.domain.User;
import com.appjam.come_with_me.dto.CreateMissionDto;
import com.appjam.come_with_me.dto.ReturnMissionDto;
import com.appjam.come_with_me.service.MissionService;
import com.appjam.come_with_me.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
public class MissionController {
    private final MissionService missionService;
    private final UserService userService;
    @GetMapping("/get")
    public ResponseEntity<?> getMissionByUser() {
        try {

            User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());

            return ResponseEntity.ok(new Result<>(user.getMissions().stream().map(ReturnMissionDto::new).toList()));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMission(@RequestBody CreateMissionDto createMissionDto) {
        try {

            Mission mission = missionService.createMission(createMissionDto);

            return ResponseEntity.ok(new Result<>(new ReturnMissionDto(mission)));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/success/{id}")
    public ResponseEntity<?> changeSuccess(@PathVariable("id") Long missionId) {
        try {

            Mission mission = missionService.changeMissionStatus(missionId);

            return ResponseEntity.ok(new Result<>(new ReturnMissionDto(mission)));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/shard/{id}")
    public ResponseEntity<?> shardMission(@PathVariable("id") Long missionId) {
        try {

            Mission mission = missionService.shardMission(missionId);

            return ResponseEntity.ok(new Result<>(new ReturnMissionDto(mission)));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
