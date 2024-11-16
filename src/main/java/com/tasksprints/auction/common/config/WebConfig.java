package com.tasksprints.auction.common.config;

import com.tasksprints.auction.common.resolver.AuthenticationResolver;
import com.tasksprints.auction.common.resolver.SearchConditionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final SearchConditionResolver searchConditionResolver;
    private final AuthenticationResolver authenticationResolver;

    public WebConfig(SearchConditionResolver searchConditionResolver, AuthenticationResolver authenticationResolver) {
        this.searchConditionResolver = searchConditionResolver;
        this.authenticationResolver = authenticationResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(searchConditionResolver);
        resolvers.add(authenticationResolver);
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
