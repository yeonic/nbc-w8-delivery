package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.user.dto.request.UserDeleteRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdateNicknameRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdatePasswordRequestDto;
import com.ateen.delivery.domain.user.dto.response.UserResponseDto;
import com.ateen.delivery.domain.user.dto.response.UserUpdateResponseDto;
import com.ateen.delivery.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    //이 방식으로는 항상 같은 Id라서 수정 필요
    @GetMapping("/{userId}")
    public UserResponseDto findUserById(@PathVariable Long userId) {
        return userService.findUserById(userId);
    }

//    @PatchMapping("/{userId}")
//    public UserUpdateResponseDto updateUser(@PathVariable Long userId, UserUpdateNicknameRequestDto updateDto) {
//        return userService.updateUser(userId, updateDto);
//    }

    @PatchMapping("/{userId}/nickname")
    public UserUpdateResponseDto updateNickname(@PathVariable Long userId,
            @Valid @RequestBody UserUpdateNicknameRequestDto dto) {
        return userService.updateNickname(userId, dto);
    }

    @PatchMapping("/{userId}/password")
    public UserUpdateResponseDto updatePassword(@PathVariable Long userId,
            @Valid @RequestBody UserUpdatePasswordRequestDto dto) {
        return userService.updatePassword(userId, dto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId, @RequestBody UserDeleteRequestDto dto) {
        userService.deleteByUserId(userId, dto);
    }
}
