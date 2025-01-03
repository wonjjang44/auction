package com.tasksprints.auction.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhisperDto {

    private String roomId;
    private long sender;
    private long receiver;
    private String message;
}
