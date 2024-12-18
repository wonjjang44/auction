package com.tasksprints.auction.domain.chat.service;

import com.tasksprints.auction.domain.chat.dto.MessageDto;
import com.tasksprints.auction.domain.chat.model.ChatRoom;
import com.tasksprints.auction.domain.user.model.User;
import java.util.List;

public interface ChatService {

    List<ChatRoom> findAllRoom();

    ChatRoom findRoomById(String id);

    User findOwnerById(String id);

    boolean isUserOwner(String id, Long user);

    void processMessage(String sender, MessageDto messageDto);

    void createRoom(String name, User owner);
}
