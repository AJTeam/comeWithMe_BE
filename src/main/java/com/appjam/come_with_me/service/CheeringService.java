package com.appjam.come_with_me.service;

import com.appjam.come_with_me.common.SecurityUtils;
import com.appjam.come_with_me.domain.Cheering;
import com.appjam.come_with_me.domain.User;
import com.appjam.come_with_me.repository.CheeringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheeringService {
    private final CheeringRepository cheeringRepository;
    private final UserService userService;

    public Cheering createCheering(String comment) {
        User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());
        return cheeringRepository.save(Cheering.builder().room(user.getRoom()).comment(comment).build());
    }

    public Page<Cheering> getCheering(Pageable pageable) {
        User user = userService.getUserByUserToken(SecurityUtils.getLoginUser().getToken());
        return cheeringRepository.getCheeringsByRoomId(user.getRoom().getId(), pageable);
    }
}
