package com.ateen.delivery.domain.auth.service;

import com.ateen.delivery.domain.auth.JwtUtil;
import com.ateen.delivery.domain.auth.dto.request.LoginRequest;
import com.ateen.delivery.domain.auth.dto.request.SignupRequest;
import com.ateen.delivery.domain.auth.dto.response.AuthResponse;
import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.config.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ClientException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ClientException(ErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserType());
        return new AuthResponse(accessToken);
    }

    public AuthResponse signup(SignupRequest dto) {
        //사전 가입 여부 확인
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ClientException(ErrorCode.EXISTING_USER_EMAIL);
        }

        if(userRepository.existsByNickname(dto.getNickname())) {
            throw new ClientException(ErrorCode.EXISTING_USER_NICKNAME);
        }

        //유저 생성, 저장
        Address address = new Address(dto.getCity(), dto.getDistrict(), dto.getStreet(), dto.getDetail());
        User user = new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getName(),
                dto.getPhoneNumber(), address, dto.getNickname(), dto.getBirthdate(),
                UserType.valueOf(dto.getUserType()));
        User saveUser = userRepository.save(user);

        String accessToken = jwtUtil.createAccessToken(saveUser.getId(), saveUser.getEmail(), saveUser.getUserType());
        return new AuthResponse(accessToken);
    }
}
