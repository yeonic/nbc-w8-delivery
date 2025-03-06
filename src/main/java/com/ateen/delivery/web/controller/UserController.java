package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.auth.annotation.Authenticate;
import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.user.dto.request.UserDeleteRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdateNicknameRequestDto;
import com.ateen.delivery.domain.user.dto.request.UserUpdatePasswordRequestDto;
import com.ateen.delivery.domain.user.dto.response.UserResponseDto;
import com.ateen.delivery.domain.user.dto.response.UserUpdateResponseDto;
import com.ateen.delivery.domain.user.service.UserService;
import com.ateen.delivery.global.argresolver.annotation.LoginUser;
import com.ateen.delivery.global.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @Authenticate
    public ResponseEntity<Response<UserResponseDto>> findUserById(@PathVariable(name = "userId") Long userId, @LoginUser AuthUser authUser) {
        return ResponseEntity.ok(Response.of(userService.findUserById(userId, authUser.getId())));
    }

    @PatchMapping("/{userId}/nickname")
    @Authenticate
    public ResponseEntity<Response<UserUpdateResponseDto>> updateNickname(
            @PathVariable(name = "userId") Long userId,
            @Valid @RequestBody UserUpdateNicknameRequestDto dto,
            @LoginUser AuthUser authUser
    ) {
        return ResponseEntity.ok(Response.of(userService.updateNickname(userId, dto, authUser.getId())));
    }

    @PatchMapping("/{userId}/password")
    @Authenticate
    public ResponseEntity<Response<UserUpdateResponseDto>> updatePassword(
            @PathVariable(name = "userId") Long userId,
            @Valid @RequestBody UserUpdatePasswordRequestDto dto,
            @LoginUser AuthUser authUser
    ) {
        return ResponseEntity.ok(Response.of(userService.updatePassword(userId, dto, authUser.getId())));
    }

    @DeleteMapping("/{userId}")
    @Authenticate
    public ResponseEntity<Void> deleteUser(
            @PathVariable(name = "userId") Long userId,
            @RequestBody UserDeleteRequestDto dto,
            @LoginUser AuthUser authUser
    ) {
        userService.deleteByUserId(userId, dto, authUser.getId());
        return ResponseEntity.noContent().build();
    }
}
