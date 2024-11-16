package com.tasksprints.auction.api.auth;

import com.tasksprints.auction.common.jwt.Auth;
import com.tasksprints.auction.domain.auth.model.Accessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    @GetMapping("/access")
    public Boolean access(@Auth Accessor accessor) {
        return true;
    }
}
