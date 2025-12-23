package com.tasksprints.auction.chat.application.service;

import com.tasksprints.auction.chat.domain.dto.ChatRoomResponse;
import com.tasksprints.auction.chat.domain.dto.MessageDto;
import com.tasksprints.auction.user.domain.dto.response.UserResponse;
import com.tasksprints.auction.user.domain.entity.User;

import java.util.List;

public interface ChatService {

    List<ChatRoomResponse> findAllRoom();

    ChatRoomResponse findRoomById(String id);

    UserResponse findOwnerById(String id);

    boolean isUserOwner(String id, Long user);

    void processMessage(String sender, MessageDto messageDto);

    void createRoom(String name, User owner);
}
