package com.ateen.delivery.domain.user.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.user.dto.request.UserDeleteRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdateNicknameRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdatePasswordRequestDto;
import com.ateen.delivery.domain.user.dto.response.UserPrivateResponseDto;
import com.ateen.delivery.domain.user.dto.response.UserResponseDto;
import com.ateen.delivery.domain.user.dto.response.UserUpdateResponseDto;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.config.PasswordEncoder;
import com.ateen.delivery.global.dto.error.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //이 방식으로는 항상 같은 Id라서 수정 필요
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(Long userId, Long authUserId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );

//        User user = userRepository.findById(authUser.getId()).orElseThrow();

        if (!userId.equals(authUserId)) {
            return new UserResponseDto(user.getEmail(), user.getName(), user.getNickname());
        }

        return UserPrivateResponseDto.privateDto(user);
    }

    @Transactional
    public UserUpdateResponseDto updateNickname(Long userId, @Valid UserUpdateNicknameRequestDto dto, Long authUserId) {

        //본인이 아닌 다른 사람은 수정할 수 없다.
        if (!userId.equals(authUserId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );

        if (userRepository.existsByNickname(dto.getNewNickname())) {
            throw new ClientException(ErrorCode.EXISTING_USER_NICKNAME);
        }

        if (dto.getNewNickname().equals(user.getNickname())) {
            throw new ClientException(ErrorCode.SAME_USER_NICKNAME);
        }

        user.updateNickname(dto.getNewNickname());
        userRepository.save(user);

        return new UserUpdateResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getPhoneNum(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public UserUpdateResponseDto updatePassword(Long userId, @Valid UserUpdatePasswordRequestDto dto, Long authUserId) {

        if (!userId.equals(authUserId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new ClientException(ErrorCode.SAME_USER_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(dto.getNewPassword()));

        //수정된 회원의 전체 정보
        return new UserUpdateResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getPhoneNum(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

    }

    @Transactional
    public void deleteByUserId(Long userId, UserDeleteRequestDto dto, Long authUserId) {

        if (!userId.equals(authUserId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ClientException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        userRepository.deleteById(userId);
    }
}
