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

    //length 기본값 255자라서 생략
    @Column(unique = true, nullable = false)
    private String email;

    //    @Column(length = 80, nullable = false)
    @Column(length = 24, nullable = false)
    private String password;

    //    @Column(length = 30, nullable = false)
    @Column(length = 22, nullable = false)
    private String name;

    //    @Column(length = 10, unique = true, nullable = false)
    @Column(length = 8, unique = true, nullable = false)
    private String nickname;

    @Embedded
    private Address address;

    //    @Column(length = 15, nullable = false) //요즘 형식에 맞춤
    @Column(length = 13, nullable = false)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'USER'")
    private UserType userType;

    private LocalDate birthDay;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean isDeleted;

    //Address를 따로 만드셨던데 컨트롤러에서 만들어서 넘겨주는 방식은 어떨까요?
    @Builder
    public User(String email, String password, String name, String phoneNum, Address address, String nickname,
            LocalDate birthDay, UserType userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
        this.address = new Address(address.getCity(), address.getDistrict(), address.getStreet(), address.getDetail());
        this.nickname = nickname;
        this.birthDay = birthDay;
        this.userType = userType;
    }
}