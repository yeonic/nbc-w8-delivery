package com.ateen.delivery.domain.auth.dto;

import com.ateen.delivery.domain.user.constants.UserType;
import lombok.Getter;

@Getter
public class AuthUser {

    private Long id;
    private String email;
    private UserType userType;

    public AuthUser(Long id, String email, UserType userType) {
        this.id = id;
        this.email = email;
        this.userType = userType;
    }
}
