package com.appjam.come_with_me.repository;

import com.appjam.come_with_me.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    Interest getInterestByName(String name);
}
