package com.tasksprints.auction.domain.chat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //클래스 레벨에서 사용
@Retention(RetentionPolicy.RUNTIME) //런타임에 참조
public @interface ChatValidation {
}
