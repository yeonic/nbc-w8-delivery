package com.ateen.delivery.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class SignupRequest {

    //회원가입시 필요한 정보
    @NotBlank
    @Size(max = 255, message = "이메일은 최대 255자여야 합니다.")
    @Pattern(regexp = "^[0-9A-Za-z._%+-]+@[0-9A-Za-z.-]+\\.[a-z]{2,6}$", message = "유효한 이메일 형식이 아닙니다.")
    private String email; // 이메일 (0-9, A-Z, a-z, -_+)

    @NotBlank
    @Size(min = 8, max = 24, message = "비밀번호는 8자 이상 24자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,24}$", message = "비밀번호는 영어 대소문자, 숫자, 특수문자를 포함해야 하며 특수문자는 +, /, \\; : - _ ^ & ( ) < > 제외해야 합니다.")
    private String password; // 비밀번호 (8~24자, 영어 대소문자, 숫자, 특수문자)

    @NotBlank
    @Size(min = 1, max = 22, message = "이름은 1자 이상 22자 이하이어야 합니다.")
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 포함해야 합니다.")
    private String name; // 이름 (한글 1~22자)

    @NotBlank
    @Size(min = 1, max = 8, message = "닉네임은 1자 이상 8자 이하이어야 하며, 이모지는 허용되지 않습니다.")
    @Pattern(regexp = "^[가-힣A-Za-z0-9]{1,8}$", message = "닉네임은 한글, 영어, 숫자만 허용됩니다.")
    private String nickname; // 닉네임 (한글, 영어, 숫자 1~8자, 이모지 불허)

    //    @NotNull(message = "주소는 필수 항목입니다.")
//    private Address address; // Address 객체를 직접 포함 // 주소 (시도 / 시군구 / 도로명 / 상세)    @NotNull(message = "주소는 필수 항목입니다.")
    @NotNull(message = "주소는 필수 항목입니다.")
    private String city;
    @NotNull(message = "주소는 필수 항목입니다.")
    private String district;
    @NotNull(message = "주소는 필수 항목입니다.")
    private String street;

    private String detail;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "핸드폰 번호 형식은 ###-####-####이어야 합니다.")
    private String phoneNumber; // 핸드폰 번호 (###-####-####)

    @NotNull
    @Pattern(regexp = "^(USER|OWNER)$", message = "유저 유형은 USER 또는 OWNER이어야 합니다.")
    private String userType; // 유저 유형 (USER | OWNER)

    @NotNull
    @Past(message = "생일은 과거 날짜여야 합니다.")
    private LocalDate birthdate; // 생일 (yyyy-MM-dd)

}
