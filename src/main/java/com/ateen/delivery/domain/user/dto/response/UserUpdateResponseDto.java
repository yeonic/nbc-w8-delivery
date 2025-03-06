package com.ateen.delivery.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserUpdateResponseDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String phoneNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public UserUpdateResponseDto(Long id, String email, String nickname, String phoneNum, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
