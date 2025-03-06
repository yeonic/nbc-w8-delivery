package com.ateen.delivery.domain.auth.service;

import com.ateen.delivery.domain.auth.JwtUtil;
import com.ateen.delivery.domain.auth.dto.AuthResponse;
import com.ateen.delivery.domain.auth.dto.LoginRequest;
import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.dto.error.ErrorCode;
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
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ClientException(ErrorCode.LOGIN_FAILED));

        // TODO : 비밀번호 검증

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserType());
        return new AuthResponse(accessToken);
    }
}
