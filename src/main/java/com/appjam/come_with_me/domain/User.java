package com.appjam.come_with_me.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.apache.commons.codec.binary.Base64;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column(name = "user_id")
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

    @OneToMany(mappedBy = "user")
    private List<Target> targets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Interest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "sendUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Feedback> sentFeedbacks;

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Feedback> receivedFeedbacks;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cheering> cheerings;


    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne(mappedBy = "adminUser")
    private Room adminRoom;

    @Column
    private String token;

    @Column
    private String img;

    @PrePersist
    private void init() {
        id = UUID.randomUUID();
        token = Base64.encodeBase64String((providerId + LocalDateTime.now().toString() + id).getBytes());
        targets = new ArrayList<>();
        interests = new ArrayList<>();
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void addTargets(Target targets) {
        this.targets.add(targets);
    }

    public void addInterests(Interest interests) {
        this.interests.add(interests);
    }

    public void addMission(Mission mission) {
        this.missions.add(mission);
    }

    public void setTargets(List<Target> targets) {
        this.targets = new ArrayList<>();
        this.targets.addAll(targets);
    }

    public void setInterests(List<Interest> interests) {
        this.interests = new ArrayList<>();
        this.interests.addAll(interests);
    }
}
