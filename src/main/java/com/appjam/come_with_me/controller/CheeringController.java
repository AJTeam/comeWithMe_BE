package com.appjam.come_with_me.controller;

import com.appjam.come_with_me.common.Result;
import com.appjam.come_with_me.domain.Cheering;
import com.appjam.come_with_me.dto.ReturnCheeringDto;
import com.appjam.come_with_me.service.CheeringService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cheering")
public class CheeringController {
    private final CheeringService cheeringService;
    @PostMapping("/create/{comment}")
    public ResponseEntity<?> createCheering(@PathVariable String comment) {
        try {

            Cheering cheering = cheeringService.createCheering(comment);

            return ResponseEntity.ok(new Result<>(new ReturnCheeringDto(cheering)));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCheering(@PageableDefault Pageable pageable) {
        try {

            Page<Cheering> cheering = cheeringService.getCheering(pageable);

            return ResponseEntity.ok(new Result<>(cheering.map(ReturnCheeringDto::new).toList()));
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(new Result<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
