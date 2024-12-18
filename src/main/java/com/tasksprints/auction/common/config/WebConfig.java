package com.tasksprints.auction.common.config;

import com.tasksprints.auction.common.resolver.ChatValidationResolver;
import com.tasksprints.auction.common.resolver.SearchConditionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final SearchConditionResolver searchConditionResolver;
    private final ChatValidationResolver chatValidationResolver;

    public WebConfig(SearchConditionResolver searchConditionResolver, ChatValidationResolver chatValidationResolver) {
        this.searchConditionResolver = searchConditionResolver;
        this.chatValidationResolver = chatValidationResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(searchConditionResolver);
        resolvers.add(chatValidationResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Authorization", "Content-Type")
            .exposedHeaders("Custom-Header")
            .maxAge(3600);
    }
}
