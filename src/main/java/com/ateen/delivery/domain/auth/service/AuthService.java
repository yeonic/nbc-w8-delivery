package com.ateen.delivery.domain.auth.service;

import com.ateen.delivery.domain.auth.JwtUtil;
import com.ateen.delivery.domain.auth.dto.AuthResponse;
import com.ateen.delivery.domain.auth.dto.LoginRequest;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    // passwordEncoder

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalStateException("이메일이 존재하지 않거나, 비밀번호가 일치하지 않습니다.")
        );

        // TODO : 비밀번호 검증

        String accessToken = jwtUtil.createAccessToken(user.getEmail());
        return new AuthResponse(accessToken);
    }
}
