package com.tasksprints.auction.chat.application.service;

import com.tasksprints.auction.chat.domain.dto.MessageDto;
import com.tasksprints.auction.chat.domain.model.ChatRoom;
import com.tasksprints.auction.user.domain.entity.User;

import java.util.List;

public interface ChatService {

    List<ChatRoom> findAllRoom();

    ChatRoom findRoomById(String id);

    User findOwnerById(String id);

    boolean isUserOwner(String id, Long user);

    void processMessage(String sender, MessageDto messageDto);

    void createRoom(String name, User owner);
}
