package com.tasksprints.auction.chat.application.service;


import com.tasksprints.auction.chat.domain.dto.AddChatRoomDto;
import com.tasksprints.auction.chat.domain.model.ChatRoom;
import com.tasksprints.auction.user.domain.entity.User;

import java.util.List;

public interface ChatService {

    List<ChatRoom> findAllRoom();

    ChatRoom findRoomById(String id);

    User findOwnerById(String id);

    boolean isUserOwner(String id, Long user);

    void createRoom(AddChatRoomDto addChatRoomDto);
}
