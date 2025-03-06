package com.ateen.delivery.domain.user.dto.response;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponseDto {

    private final String email;
    private final String name;
    private final String nickname;

    public UserResponseDto(String email, String name, String nickname) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
    }
}
