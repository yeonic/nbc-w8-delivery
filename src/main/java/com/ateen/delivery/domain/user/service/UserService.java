package com.ateen.delivery.domain.user.service;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.dto.request.UserSaveRequestDto;
import com.ateen.delivery.domain.user.dto.response.UserResponseDto;
import com.ateen.delivery.domain.user.dto.response.UserSaveResponseDto;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
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
//    private final PasswordEncoder passwordEncoder;

    @Transactional
    //오류 메세지 Enum으로 해야하는데 어떻게 하는 지 모르겠음.
    public UserSaveResponseDto save(@Valid UserSaveRequestDto dto) {

        //사전 가입 여부 확인
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        //유저 생성, 저장
        Address address = new Address(dto.getCity(), dto.getDistrict(), dto.getStreet(), dto.getDetail());
        User user = new User(dto.getEmail(), dto.getPassword(), dto.getName(), dto.getPhoneNumber(), address, dto.getNickname(), dto.getBirthdate(), UserType.valueOf(dto.getUserType()));
        User saveUser = userRepository.save(user);

        //객체 생성하는 static 메서드 있음
        return UserSaveResponseDto.buildDto(saveUser);
    }


    @Transactional
    public List<UserResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponseDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(new UserResponseDto(user.getEmail(), user.getName(), user.getNickname()));
        }

//        //인증인가 되면 사용할 로직
//        for (User user : users) {
//            //자기 정보일 시 전부 반환
//            if (userId.equals(user.getUserId())) {
//                dtos.add(new UserPrivateResponseDto(user.getEmail(), user.getName(), user.getNickname(), user.getUserId(), user.getPhoneNum(), user.getUserType(), user.getAddress(), user.getBirthDay(), user.getCreatedAt(), user.getUpdatedAt()));
//            }
//            dtos.add(new UserResponseDto(user.getEmail(), user.getName(), user.getNickname()));
//        }
        return dtos;
    }
}
