package com.ateen.delivery.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    //로그인 할 때 필요한 정보
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;
}
