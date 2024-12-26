package com.tasksprints.auction.bid.application.resolver;

import com.tasksprints.auction.bid.application.annotation.BidValidation;
import com.tasksprints.auction.bid.application.service.BidService;
import com.tasksprints.auction.bid.domain.dto.BidRequest;
import com.tasksprints.auction.chat.application.service.ChatService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class BidValidationResolver implements HandlerMethodArgumentResolver {

    private final ChatService chatService;
    private final BidService bidService;

    public BidValidationResolver(ChatService chatService, BidService bidService) {
        this.chatService = chatService;
        this.bidService = bidService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAnnotationPresent(BidValidation.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) {
        long userId = Long.parseLong(Objects.requireNonNull(webRequest.getParameter("userId")));
        long auctionId = Long.parseLong(Objects.requireNonNull(webRequest.getParameter("auctionId")));
        String chatRoomId = webRequest.getParameter("chatRoomId");
        BigDecimal amount = new BigDecimal(Objects.requireNonNull(webRequest.getParameter("amount")));

        BidRequest bidRequest = new BidRequest();
        bidRequest.setUserId(userId);
        bidRequest.setAuctionId(auctionId);
        bidRequest.setChatRoomId(chatRoomId);
        bidRequest.setAmount(amount);

        validate(bidRequest);

        return bidRequest;
    }

    private void validate(BidRequest bidRequest) {
        if (chatService.isUserOwner(bidRequest.getChatRoomId(), bidRequest.getUserId())) {
            throw new IllegalArgumentException("Owner can't enter any chatting");
        }
        if (bidService.isBidEnd(bidRequest.getAuctionId())) {
            throw new IllegalArgumentException("Bid Already Ended.");
        }
    }
}
