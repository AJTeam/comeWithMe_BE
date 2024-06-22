package com.appjam.come_with_me.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User {
    @Id
    @Column(name = "users_id")
    private UUID id;

    @Column
    private String nickname;

    @Column
    private int age;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String email;

    @Column
    private String provider;

    @Column
    private String providerId;

    @OneToMany
    @JoinColumn(name = "users_id")
    private List<Target> targets;

    @OneToMany
    @JoinColumn(name = "users_id")
    private List<Interest> interests;

    @Column
    private String token;

    @PrePersist
    private void init() {
        id = UUID.randomUUID();
        token = Base64.encodeBase64String((providerId + LocalDateTime.now().toString() + id).getBytes());
    }
}
