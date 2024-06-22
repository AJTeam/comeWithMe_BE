package com.appjam.come_with_me.service;

import com.appjam.come_with_me.domain.*;
import com.appjam.come_with_me.dto.RegisterUserDto;
import com.appjam.come_with_me.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TargetService targetService;
    private final InterestService interestService;

    @Value("${google.clientKey}")
    private String clientKey;

    public User getUserByProviderId(String providerId) {
        return userRepository.getUserByProviderId(providerId).orElseThrow(
                () -> new IllegalStateException("유저를 찾지 못하였습니다.")
        );
    }

    public User getUserByUserToken(String token) {
        return userRepository.getUserByToken(token).orElseThrow(
                () -> new IllegalStateException("유저를 찾지 못하였습니다.")
        );
    }

    @Transactional
    public User testRegister() {
        User user = User.builder()
                .email("asdf")
                .provider("asdf")
                .providerId("adsfasdf")
                .role(Role.USER)
                .nickname("asdf")
                .age(19)
                .gender(Gender.MALE)
                .interests(null)
                .targets(null)
                .build();

        return userRepository.save(user);
    }

    public String login(String idToken) throws GeneralSecurityException, IOException {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientKey))
                .build();

        GoogleIdToken token = verifier.verify(idToken);
        if (token != null) {
            String providerId = token.getPayload().getSubject();

            try {
                User user = getUserByProviderId(providerId);
                return user.getToken();
            }catch (IllegalStateException e) {
                return "회원가입이 필요합니다.";
            }
        }
        throw new IllegalStateException("Invalid Token Exception");
    }

    @Transactional
    public String registerUser(String idToken, RegisterUserDto registerUserDto) {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientKey))
                .build();
        GoogleIdToken token = null;
        try {
            token = verifier.verify(idToken);
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        List<Target> targets = new ArrayList<>();
        List<Interest> interests = new ArrayList<>();

        for (Map<String, String> targetName : registerUserDto.getTargets()) {
            Target target = targetService.getTargetByName(targetName.get("name"));
            if(target == null) {
                target = targetService.saveTarget(targetName.get("name"));
            }
            targets.add(target);
        }

        for (Map<String, String> interestName : registerUserDto.getInterests()) {
            Interest interest = interestService.getInterestByName(interestName.get("name"));
            if (interest == null) {
                interest = interestService.saveInterest(interestName.get("name"));
            }
            interests.add(interest);
        }

        User user = User.builder()
                .age(registerUserDto.getAge())
                .gender(registerUserDto.getGender())
                .providerId(token.getPayload().getSubject())
                .email(token.getPayload().getEmail())
                .role(Role.USER)
                .nickname(registerUserDto.getUsername())
                .targets(targets)
                .interests(interests)
                .provider("google")
                .build();

        return userRepository.save(user).getToken();
    }
}
