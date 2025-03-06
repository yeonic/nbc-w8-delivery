package com.ateen.delivery.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateNicknameRequestDto {

    @NotBlank
    @Size(min = 1, max = 8, message = "닉네임은 1자 이상 8자 이하이어야 하며, 이모지는 허용되지 않습니다.")
    @Pattern(regexp = "^[가-힣A-Za-z0-9]{1,8}$", message = "닉네임은 한글, 영어, 숫자만 허용됩니다.")
    private String newNickname; // 닉네임 (한글, 영어, 숫자 1~8자, 이모지 불허)

}
