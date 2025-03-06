package com.ateen.delivery.domain.user.service;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    //오류 메세지 Enum으로 해야하는데 어떻게 하는 지 모르겠음.
    public UserSaveResponseDto save(@Valid UserSaveRequestDto dto) {

        //사전 가입 여부 확인
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        //유저 생성, 저장
        Address address = new Address(dto.getCity(), dto.getDistrict(), dto.getStreet(), dto.getDetail());
        User user = new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getName(), dto.getPhoneNumber(), address, dto.getNickname(), dto.getBirthdate(), UserType.valueOf(dto.getUserType()));
        User saveUser = userRepository.save(user);

        //객체 생성하는 static 메서드 있음
        return UserSaveResponseDto.buildDto(saveUser);
    }


    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserResponseDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(new UserResponseDto(user.getEmail(), user.getName(), user.getNickname()));
        }
        return dtos;
    }

    //이 방식으로는 항상 같은 Id라서 수정 필요
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(Long userId) {

        User findUser = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("해당 아이디가 없습니다.")
        );

        //본인 아이디 외에는 기본 정보만 반환
        if (!userId.equals(findUser.getId())) {
            return new UserResponseDto(findUser.getEmail(), findUser.getName(), findUser.getNickname());
        }
        //본인 아이디는 전체 조회
        return UserPrivateResponseDto.privateDto(findUser);

    }

    @Transactional
    public UserUpdateResponseDto updateNickname(Long userId, @Valid UserUpdateNicknameRequestDto dto) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("해당 아이디가 없습니다.")
        );

        if (user.getNickname().equals(dto.getNewNickname())) {
            throw new IllegalStateException("같은 닉네임으로 변경할 수 없습니다.");
        }

        user.updateNickname(dto.getNewNickname());
        userRepository.save(user);
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

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("해당 아이디가 없습니다.")
        );

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new IllegalStateException("같은 비밀번호로 변경할 수 없습니다.");
        }

        user.updatePassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
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

//        if (!userRepository.existsById(userId)) {
//            throw new IllegalStateException("해당 아이디가 없습니다.");
//        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("해당 아이디가 없습니다.")
        );

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않아 탈퇴할 수 없습니다.");
        }

        userRepository.deleteById(userId);

    }
}
