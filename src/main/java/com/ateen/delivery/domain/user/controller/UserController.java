package com.ateen.delivery.domain.user.controller;

//import com.ateen.delivery.domain.auth.JwtUtil;
import com.ateen.delivery.domain.auth.JwtUtil;
import com.ateen.delivery.domain.user.dto.request.UserDeleteRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserSaveRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdateNicknameRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdatePasswordRequestDto;
import com.ateen.delivery.domain.user.dto.response.UserResponseDto;
import com.ateen.delivery.domain.user.dto.response.UserSaveResponseDto;
import com.ateen.delivery.domain.user.dto.response.UserUpdateResponseDto;
import com.ateen.delivery.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public UserSaveResponseDto save(@Valid @RequestBody UserSaveRequestDto dto) {
        return userService.save(dto);
    }

    //Todo 우선 그냥 조회하고, HttpServletRequest.getAttribute()를 이용한 조회로 다시 바꿔주기
    //Todo 본인 정보만 상세조회, 타인 정보는 제한적 조회
    @GetMapping
    public List<UserResponseDto> findAllUsers() {
        return userService.findAllUsers();
    }

    /// /    인증인가 되면 사용할 로직
//    public List<UserResponseDto> findAllUser(HttpServletRequest request) {
//        Long userId = Long.parseLong((String) request.getAttribute("id"));
//        return userService.findAllUser(userId);
//    }

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
    public UserUpdateResponseDto updateNickname(@PathVariable Long userId, @Valid @RequestBody UserUpdateNicknameRequestDto dto) {
        return userService.updateNickname(userId, dto);
    }

    @PatchMapping("/{userId}/password")
    public UserUpdateResponseDto updatePassword(@PathVariable Long userId, @Valid @RequestBody UserUpdatePasswordRequestDto dto) {
        return userService.updatePassword(userId, dto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId, @RequestBody UserDeleteRequestDto dto) {
        userService.deleteByUserId(userId, dto);
    }
}
