package com.tasksprints.auction.auction.application.resolver;

import com.tasksprints.auction.chat.application.service.ChatService;
import com.tasksprints.auction.chat.domain.dto.MessageDto;
import com.tasksprints.auction.domain.chat.annotation.ChatValidation;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
public class ChatValidationResolver implements HandlerMethodArgumentResolver {

    private final ChatService chatService;

    public ChatValidationResolver(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAnnotationPresent(ChatValidation.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String type = webRequest.getParameter("type");
        String roomId = webRequest.getParameter("roomId");
        Long sender = Long.valueOf(Objects.requireNonNull(webRequest.getParameter("sender")));
        String msg = webRequest.getParameter("message");

        MessageDto messageDto = new MessageDto();
        messageDto.setType(type);
        messageDto.setRoomId(roomId);
        messageDto.setSender(sender);
        messageDto.setMessage(msg);

        validate(messageDto); //메시지 검증

        return messageDto;
    }

    private void validate(MessageDto messageDto) {
        if (chatService.isUserOwner(messageDto.getRoomId(), messageDto.getSender())) {
            throw new IllegalArgumentException("Owner can't enter any chatting");
        }
        //이외의 메시지 검증 로직 추가
    }
}
