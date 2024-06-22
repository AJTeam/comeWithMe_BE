package com.appjam.come_with_me.service;

import com.appjam.come_with_me.domain.Interest;
import com.appjam.come_with_me.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InterestService {
    private final InterestRepository interestRepository;

    public Interest getInterestByName(String name) {
        return interestRepository.getInterestByName(name);
    }

    public Interest saveInterest(String name) {
        return interestRepository.save(Interest.builder().name(name).build());
    }
}
