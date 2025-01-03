package com.tasksprints.auction.chat.application.resolver;

import com.tasksprints.auction.chat.application.service.ChatService;
import com.tasksprints.auction.chat.application.annotation.WhisperValidation;
import com.tasksprints.auction.chat.domain.dto.WhisperDto;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
public class WhisperValidationResolver implements HandlerMethodArgumentResolver {

    private final ChatService chatService;

    public WhisperValidationResolver(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAnnotationPresent(WhisperValidation.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String roomId = webRequest.getParameter("roomId");
        long sender = Long.parseLong(Objects.requireNonNull(webRequest.getParameter("sender")));
        long receiver = Long.parseLong(Objects.requireNonNull(webRequest.getParameter("receiver")));
        String message = webRequest.getParameter("message");

        WhisperDto whisperDto = new WhisperDto();
        whisperDto.setRoomId(roomId);
        whisperDto.setSender(sender);
        whisperDto.setReceiver(receiver);
        whisperDto.setMessage(message);

        validate(whisperDto);

        return whisperDto;
    }

    private void validate(WhisperDto whisperDto) {
        if (chatService.isUserOwner(whisperDto.getRoomId(), whisperDto.getSender())) {
            throw new IllegalArgumentException("Owner can't enter any chatting");
        }
        //이외의 메시지 검증 로직 추가
    }
}
