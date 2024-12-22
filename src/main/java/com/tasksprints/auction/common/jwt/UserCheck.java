package com.tasksprints.auction.common.jwt;

import com.tasksprints.auction.domain.auth.exception.AuthException;
import com.tasksprints.auction.domain.auth.model.Accessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserCheck {
    @Before("@annotation(UserOnly)")
    public void userCheck(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof Accessor accessor) {
                if (!accessor.isUser()) {
                    throw new AuthException("Invalid Access");
                }
            }
        }
    }
}
