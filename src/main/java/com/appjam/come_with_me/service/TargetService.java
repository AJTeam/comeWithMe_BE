package com.appjam.come_with_me.service;

import com.appjam.come_with_me.domain.Target;
import com.appjam.come_with_me.domain.User;
import com.appjam.come_with_me.repository.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class TargetService {
    private final TargetRepository targetRepository;

    public Target getTargetByName(String targetName) {
        return targetRepository.getTargetByName(targetName);
    }

    public Target saveTarget(String name) {
        return targetRepository.save(Target.builder().name(name).build());
    }

    public Target saveTarget(String name, User user) {
        return targetRepository.save(Target.builder().name(name).user(user).build());
    }

    public void save(Target target) {
        targetRepository.save(target);
    }
}
