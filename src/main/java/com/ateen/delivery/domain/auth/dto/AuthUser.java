package com.ateen.delivery.domain.auth.dto;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import lombok.Getter;

@Getter
public class AuthUser {

    private Long id;
    private UserType userType;
    private String name;
    private String nickname;
    private Address address;

    public AuthUser(Long id, UserType userType, String name, String nickname, Address address) {
        this.id = id;
        this.userType = userType;
        this.name = name;
        this.nickname = nickname;
        this.address = new Address(address.getCity(), address.getDistrict(), address.getStreet(), address.getDetail());
    }

    public static AuthUser fromUser(User user) {
        return new AuthUser(
                user.getUserId(), user.getUserType(), user.getName(), user.getNickname(), user.getAddress());
    }
}
