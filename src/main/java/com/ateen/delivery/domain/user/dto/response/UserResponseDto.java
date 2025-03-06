package com.ateen.delivery.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponseDto {

    //기존 요구한 필드값
    private final String email;
    private final String name;
    private final String nickname;

    public UserResponseDto(String email, String name, String nickname) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
    }
}
