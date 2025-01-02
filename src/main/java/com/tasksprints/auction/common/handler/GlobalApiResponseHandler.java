package com.tasksprints.auction.common.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tasksprints.auction.common.constant.ApiResponseMessage;
import com.tasksprints.auction.common.response.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalApiResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Autowired
    public GlobalApiResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        // JSON Pretty Printing 활성화
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    /**
     * Swagger를 제외한 모든 응답에 대하여 처리
     *
     * @param returnType
     * @param converterType
     *
     * @return boolean
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Swagger 제외
        HttpServletRequest swaggerRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestURI = swaggerRequest.getRequestURI();

        // Socket Message, JWT 관련 논의 필요

        return !requestURI.contains("/swagger") && !requestURI.contains("/v1/api-docs");
    }


    /**
     * 컨트롤러 메서드들이 리턴한 값을 가로채 처리
     * ResponseEntity.ok(ApiResult.success()) 공통 래핑 작업 수행
     *
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     *
     * @return Object
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String message = "successfully";

        // @ApiResponseMessage 어노테이션이 존재하는지 확인
        if(returnType.getContainingClass().isAnnotationPresent(ApiResponseMessage.class) || returnType.hasMethodAnnotation(ApiResponseMessage.class)) {
            ApiResponseMessage annotation = returnType.getMethodAnnotation(ApiResponseMessage.class);

            if(annotation != null) {
                // 어노테이션에 설정된 메시지 값을 가져온다
                message = annotation.value();
            }
        }

        // 리턴 타입이 String 일 경우 ClassCastException 발생
        if(body instanceof String) {
            // ApiResult 로 래핑 후 JSON 형식 문자열로 변환
            try {
                log.debug("objectMapper => {}", objectMapper);

                // body를 직접 리턴하면 JSON 형식으로 일관되게 제공되지 않으므로 JSON으로 변환
                return objectMapper.writeValueAsString(ResponseEntity.ok(ApiResult.success(message, body)).getBody());
            } catch (JsonProcessingException e) {
                log.debug("Exception => {}", e.getMessage());

                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok(ApiResult.success(message, body)).getBody();
    }

}
