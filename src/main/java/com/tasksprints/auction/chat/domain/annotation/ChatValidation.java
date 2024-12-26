package com.tasksprints.auction.chat.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE}) //파라미터에서 사용
@Retention(RetentionPolicy.RUNTIME) //런타임에 참조
public @interface ChatValidation {
}
