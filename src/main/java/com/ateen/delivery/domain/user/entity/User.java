package com.ateen.delivery.domain.user.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.user.constants.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 80, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 10, unique = true, nullable = false)
    private String nickname;

    @Embedded
    private Address address;

    @Column(length = 15, nullable = false)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'USER'")
    private UserType userType;

    private LocalDate birthDay;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean isDeleted;

    @Builder
    public User(String email, String password, String name, String nickname, String city, String district,
            String street, String detail, String phoneNum, UserType userType, LocalDate birthDay) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.userType = userType;
        this.birthDay = birthDay;
        this.address = (city == null || district == null || street == null)
                ? null
                : new Address(city, district, street, detail);
    }
}
