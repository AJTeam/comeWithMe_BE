package com.appjam.come_with_me.repository;

import com.appjam.come_with_me.domain.Target;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Target getTargetByName(String name);
}
