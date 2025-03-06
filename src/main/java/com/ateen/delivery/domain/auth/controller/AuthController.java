package com.ateen.delivery.domain.auth.controller;

import com.ateen.delivery.domain.auth.dto.AuthResponse;
import com.ateen.delivery.domain.auth.dto.LoginRequest;
import com.ateen.delivery.domain.auth.service.AuthService;
import com.ateen.delivery.global.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<Response<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequestDto) {
        return ResponseEntity.ok(Response.of(service.login(loginRequestDto)));
    }
//
//    @PostMapping("/logout")
//    private ResponseEntity<Void> logout() {
//
//    }
}
