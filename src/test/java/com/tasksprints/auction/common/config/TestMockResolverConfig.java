package com.tasksprints.auction.common.config;

import com.tasksprints.auction.bid.application.resolver.BidValidationResolver;
import com.tasksprints.auction.chat.application.resolver.ChatValidationResolver;
import com.tasksprints.auction.chat.application.resolver.WhisperValidationResolver;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestMockResolverConfig {
    @MockBean
    public BidValidationResolver bidValidationResolver;
    @MockBean
    public ChatValidationResolver chatValidationResolver;
    @MockBean
    public WhisperValidationResolver whisperValidationResolver;
}
