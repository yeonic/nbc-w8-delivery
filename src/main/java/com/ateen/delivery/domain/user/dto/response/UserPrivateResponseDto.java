package com.ateen.delivery.domain.user.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.user.constants.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class UserPrivateResponseDto extends UserResponseDto {
    private final Long id;
    private final String phoneNum;
    private final UserType userType; // Enum의 String 표현 사용
    private final Address address;
    private final LocalDate birthDay;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public UserPrivateResponseDto(String email, String name, String nickname, Long id, String phoneNum, UserType userType, Address address, LocalDate birthDay, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(email, name, nickname);
        this.id = id;
        this.phoneNum = phoneNum;
        this.userType = userType;
        this.address = address;
        this.birthDay = birthDay;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
