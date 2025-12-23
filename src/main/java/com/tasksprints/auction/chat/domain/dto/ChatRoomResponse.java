package com.tasksprints.auction.chat.domain.dto;

import com.tasksprints.auction.chat.domain.model.ChatRoom;
import com.tasksprints.auction.user.domain.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {
    String chatRoomId;
    String name;
    UserResponse owner;
    List<UserResponse> users;

    public static ChatRoomResponse of(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
            .chatRoomId(chatRoom.getChatRoomId())
            .name(chatRoom.getName())
            .owner(UserResponse.builder()
                .id(chatRoom.getOwner().getId())
                .nickName(chatRoom.getOwner().getNickName())
                .build())
            .users(chatRoom.getUsers().stream()
                .map(user -> UserResponse.builder()
                    .id(user.getId())
                    .nickName(user.getNickName())
                    .build())
                .collect(Collectors.toList()))
            .build();
    }
}
