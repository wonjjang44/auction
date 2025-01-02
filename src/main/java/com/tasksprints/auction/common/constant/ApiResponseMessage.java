package com.tasksprints.auction.common.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 메서드에 붙일 수 있도록 지정
@Retention(RetentionPolicy.RUNTIME) // 런타임 시 어노테이션 유지
public @interface ApiResponseMessage {
    String value();

}
