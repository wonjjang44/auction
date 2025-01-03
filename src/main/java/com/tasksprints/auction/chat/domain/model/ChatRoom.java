package com.tasksprints.auction.chat.domain.model;
import com.tasksprints.auction.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "chatRoomId")
    private String chatRoomId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany
    @Column(name = "chat_users")
    private List<User> users;

    @Builder
    public ChatRoom(String name, User owner) {
        this.chatRoomId = UUID.randomUUID().toString();
        this.name = name;
        this.owner = owner;
        this.users = new ArrayList<>();
    }
}
