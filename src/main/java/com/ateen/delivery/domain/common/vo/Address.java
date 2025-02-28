package com.ateen.delivery.domain.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(length = 10, nullable = false)
    private String city;

    @Column(length = 10, nullable = false)
    private String district;

    @Column(length = 10, nullable = false)
    private String street;

    @Column(length = 40)
    private String detail;
}
