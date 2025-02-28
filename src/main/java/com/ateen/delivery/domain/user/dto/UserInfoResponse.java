package com.ateen.delivery.domain.user.dto;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoResponse {

    private Long id;
    private String email;
    private String name;
    private String nickname;
    private Address address;
    private String phoneNum;
    private UserType userType;
    private LocalDate birthDay;

    public static UserInfoResponse fromUser(User user) {
        Address newAddress = new Address(
                user.getAddress().getCity(), user.getAddress().getDistrict(),
                user.getAddress().getStreet(), user.getAddress().getDetail());

        return new UserInfoResponse(
                user.getId(), user.getEmail(), user.getName(), user.getNickname(), newAddress,
                user.getPhoneNum(), user.getUserType(), user.getBirthDay());
    }
}
