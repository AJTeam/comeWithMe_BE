package com.appjam.come_with_me.repository;

import com.appjam.come_with_me.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
