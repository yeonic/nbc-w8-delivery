package com.ateen.delivery.domain.user.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.dto.request.UserDeleteRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserSaveRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdateNicknameRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdatePasswordRequestDto;
import com.ateen.delivery.domain.user.dto.response.UserPrivateResponseDto;
import com.ateen.delivery.domain.user.dto.response.UserResponseDto;
import com.ateen.delivery.domain.user.dto.response.UserSaveResponseDto;
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

    @Transactional
    public UserSaveResponseDto save(@Valid UserSaveRequestDto dto) {

        //사전 가입 여부 확인
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ClientException(ErrorCode.EXISTING_USER_EMAIL);
        }

        //유저 생성, 저장
        Address address = new Address(dto.getCity(), dto.getDistrict(), dto.getStreet(), dto.getDetail());
        User user = new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getName(),
                dto.getPhoneNumber(), address, dto.getNickname(), dto.getBirthdate(),
                UserType.valueOf(dto.getUserType()));
        User saveUser = userRepository.save(user);

        //객체 생성하는 static 메서드 있음
        return UserSaveResponseDto.buildDto(saveUser);
    }

    //이 방식으로는 항상 같은 Id라서 수정 필요
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        //본인 아이디 외에는 기본 정보만 반환
        if (!userId.equals(user.getId())) {
            return new UserResponseDto(user.getEmail(), user.getName(), user.getNickname());
        }
        //본인 아이디는 전체 조회
        return UserPrivateResponseDto.privateDto(user);
    }

    @Transactional
    public UserUpdateResponseDto updateNickname(Long userId, @Valid UserUpdateNicknameRequestDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        if (user.getNickname().equals(dto.getNewNickname())) {
            throw new ClientException(ErrorCode.SAME_USER_NICKNAME);
        }

        user.updateNickname(dto.getNewNickname());

        //수정된 회원의 전체 정보
        return new UserUpdateResponseDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getPhoneNum(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

    }


    @Transactional
    public UserUpdateResponseDto updatePassword(Long userId, @Valid UserUpdatePasswordRequestDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new ClientException(ErrorCode.SAME_USER_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(dto.getNewPassword()));

        //수정된 회원의 전체 정보
        return new UserUpdateResponseDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getPhoneNum(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

    }

    @Transactional
    public void deleteByUserId(Long userId, UserDeleteRequestDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ClientException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        userRepository.deleteById(userId);
    }
}
