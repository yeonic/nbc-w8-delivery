package com.ateen.delivery.domain.user.dto.response;

import com.ateen.delivery.domain.user.constants.UserType;

public class UserPublicResponseDto extends UserResponseDto {

    public UserPublicResponseDto(String email, String name, String nickname) {
        super(email, name, nickname);
    }
}
