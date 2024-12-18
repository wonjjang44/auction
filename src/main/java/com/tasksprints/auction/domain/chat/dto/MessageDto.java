package com.tasksprints.auction.domain.chat.dto;

import com.tasksprints.auction.domain.chat.annotation.ChatValidation;
import lombok.*;

@Data
@ChatValidation
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    public enum MessageType {
        ENTER, TALK, LEAVE
    }

    private MessageType type;
    private String roomId;
    private Long sender;
    private String message;

    public void setType(String type) {
        this.type = MessageType.valueOf(type);
    }
}
