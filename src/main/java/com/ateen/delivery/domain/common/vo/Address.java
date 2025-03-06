package com.ateen.delivery.domain.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Address {

    @NotBlank
    @Column(length = 10, nullable = false)
    private String city;

    @NotBlank
    @Column(length = 10, nullable = false)
    private String district;

    @NotBlank
    @Column(length = 10, nullable = false)
    private String street;

    @Column(length = 40)
    private String detail;

    public static Address clone(Address old) {
        return new Address(old.getCity(), old.getDistrict(), old.getStreet(), old.getDetail());
    }
}
