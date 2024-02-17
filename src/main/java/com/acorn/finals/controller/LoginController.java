package com.acorn.finals.controller;

import com.acorn.finals.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final JwtService jwtService;

    @GetMapping("/login")
    public String login() {
        return jwtService.generateJwsWithString("hohoho");
    }
}