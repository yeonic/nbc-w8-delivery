package com.ateen.delivery.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequest {

    //로그인 할 때 필요한 정보
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Pattern(regexp = "^[0-9A-Za-z._%+-]+@[0-9A-Za-z.-]+\\.[a-zA-Z]{2,6}$", message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;
}
