package com.appjam.come_with_me.controller;

import com.appjam.come_with_me.common.Result;
import com.appjam.come_with_me.common.SecurityUtils;
import com.appjam.come_with_me.domain.User;
import com.appjam.come_with_me.dto.RegisterUserDto;
import com.appjam.come_with_me.dto.ReturnUserDto;
import com.appjam.come_with_me.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response) {
        String idToken = request.getHeader("idToken");

        try {
            String token = userService.login(idToken);
            response.setHeader("token", token);
            return ResponseEntity.ok(new Result<>("로그인 성공하였습니다."));
        }catch (GeneralSecurityException | IOException e) {
            return new ResponseEntity<>(new Result<>("idToken 검증에 실패하였습니다."), HttpStatus.UNAUTHORIZED);
        }catch (IllegalStateException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/register"));
            return new ResponseEntity<>(new Result<>("회원가입이 필요합니다."), HttpStatus.MOVED_PERMANENTLY);
        }catch (Exception e) {
            return new ResponseEntity<>(new Result<>("알수 없는 에러가 발생하였습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(new ReturnUserDto(userService.testRegister()));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto, HttpServletRequest request, HttpServletResponse response) {
        String idToken = request.getHeader("idToken");

        if (idToken == null || idToken.isBlank())
            return new ResponseEntity<>(new Result<>("idToken 헤더 값이 비었습니다."), HttpStatus.BAD_REQUEST);

        String token = userService.registerUser(idToken, registerUserDto);
        response.setHeader("token", token);
        return ResponseEntity.ok(new Result<>("회원가입이 완료되었습니다."));
    }
    @GetMapping("/user/info")
    public ResponseEntity<?> getUserInfo() {
        User user = SecurityUtils.getLoginUser();

        User fetchUser = userService.getUserByUserToken(user.getToken());

        return ResponseEntity.ok(new Result<>(new ReturnUserDto(fetchUser)));
    }

}
