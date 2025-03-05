package com.ateen.delivery.domain.auth.controller;

import com.ateen.delivery.domain.auth.dto.JwtTokenDto;
import com.ateen.delivery.domain.auth.service.AuthService;
import com.ateen.delivery.domain.auth.dto.LoginRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/login")
//    public JwtTokenDto login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
//
//    }
}
