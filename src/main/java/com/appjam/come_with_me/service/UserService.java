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
import java.util.*;

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
                .build();


        userRepository.save(user);

        Interest interest = Interest.builder().name("쇼츠").user(user).build();
        Interest interest2 = Interest.builder().name("릴스").user(user).build();
        Target target = Target.builder().name("갓생").user(user).build();

        targetService.save(target);
        interestService.save(interest);
        interestService.save(interest2);

        user.setInterests(Arrays.asList(interest, interest2));
        user.setTargets(Collections.singletonList(target));

        userRepository.save(user);

        return user;
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

        User user = User.builder()
                .age(registerUserDto.getAge())
                .gender(registerUserDto.getGender())
                .providerId(token.getPayload().getSubject())
                .email(token.getPayload().getEmail())
                .role(Role.USER)
                .nickname(registerUserDto.getUsername())
                .img((String) token.getPayload().get("picture"))
//                .targets(targets)
//                .interests(interests)
                .provider("google")
                .build();

        userRepository.save(user);

        List<Target> targets = new ArrayList<>();
        List<Interest> interests = new ArrayList<>();

        for (Map<String, String> targetName : registerUserDto.getTargets()) {
            Target target = targetService.saveTarget(targetName.get("name"), user);
            targets.add(target);
        }

        for (Map<String, String> interestName : registerUserDto.getInterests()) {
            Interest interest = interestService.saveInterest(interestName.get("name"), user);
            interests.add(interest);
        }

        user.setInterests(interests);
        user.setTargets(targets);
        userRepository.save(user);

        return user.getToken();
    }
}
