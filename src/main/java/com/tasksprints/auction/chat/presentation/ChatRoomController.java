package com.tasksprints.auction.chat.presentation;
import com.tasksprints.auction.chat.application.service.ChatService;
import com.tasksprints.auction.chat.domain.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat/room")
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping("/all")
    public List<ChatRoom> chatRoomAll() {
        return chatService.findAllRoom();
    } //채팅방 목록 조회

    @GetMapping("/{chatRoomId}")
    public ChatRoom chatRoom(@PathVariable(value = "chatRoomId") String chatRoomId) {
        return chatService.findRoomById(chatRoomId);
    } //채팅방 조회
}
