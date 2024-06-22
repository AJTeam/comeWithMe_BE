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
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "room_id")
    private UUID id;

    @Column
    private String name;

    @Column
    private String title;

    @OneToMany(mappedBy = "room")
    private List<Target> targets;

    @OneToMany(mappedBy = "room")
    private List<Interest> interests;

    @OneToMany(mappedBy = "room")
    private List<User> users;

    @OneToOne
    @JoinColumn(name = "admin_user_id")
    private User adminUser;

    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
