package com.tasksprints.auction.chat.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.tasksprints.auction.chat.domain.dto.ChatRoomResponse;
import com.tasksprints.auction.chat.domain.dto.MessageDto;
import com.tasksprints.auction.chat.domain.model.ChatRoom;
import com.tasksprints.auction.chat.infrastructure.ChatRoomRepository;
import com.tasksprints.auction.user.domain.dto.response.UserResponse;
import com.tasksprints.auction.user.domain.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ObjectMapper mapper;
    private ConcurrentHashMap<String, ChatRoom> chatRoomMap;
    private final ChatRoomRepository chatRoomRepository;

    @PostConstruct
    private void init() {
        chatRoomMap = new ConcurrentHashMap<>();
    }

    @Transactional
    @Override
    public List<ChatRoomResponse> findAllRoom() {
        List<ChatRoom> chatRooms = new ArrayList<>(chatRoomMap.values());
        return chatRooms.stream().map(ChatRoomResponse::of).toList();
    }

    @Transactional
    @Override
    public ChatRoomResponse findRoomById(String id) {
        ChatRoom chatRoom = chatRoomMap.get(id);
        return ChatRoomResponse.of(chatRoom);
    }

    @Override
    public UserResponse findOwnerById(String id) {
        return findRoomById(id).getOwner();
    }

    @Override
    public boolean isUserOwner(String id, Long user) {
        return findOwnerById(id).getId().equals(user);
    }

    @Override
    public void processMessage(String sender, MessageDto messageDto) {
        switch (messageDto.getType()) {
            case ENTER -> {
                messageDto.setMessage(sender + "님이 입장하셨습니다.");
                break;
            }
            case LEAVE -> {
                messageDto.setMessage(sender + "님이 퇴장하셨습니다.");
                break;
            }
            default -> messageDto.setMessage(sender + " : " + messageDto.getMessage());
        }
    }

    @Transactional
    @Override
    public void createRoom(String name, User owner) {
        ChatRoom chatRoom = ChatRoom.builder()
            .name(name)
            .owner(owner)
            .build();
        chatRoomRepository.save(chatRoom);
        log.info("Create Room : {} {}", chatRoom.getId(), chatRoom.getName());
        chatRoomMap.put(chatRoom.getChatRoomId(), chatRoom);
    }
}
