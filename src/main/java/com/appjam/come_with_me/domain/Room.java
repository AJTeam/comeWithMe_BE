package com.appjam.come_with_me.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "room_id")
    private UUID id;

    @Column
    private String name;

    @OneToMany
    @JoinColumn(name = "room_id")
    private List<Target> targets;

    @OneToMany
    @JoinColumn(name = "room_id")
    private List<Interest> interests;

    @OneToMany
    @JoinColumn(name = "room_id")
    private List<User> users;

}
