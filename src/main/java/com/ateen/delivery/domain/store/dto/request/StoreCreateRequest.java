package com.ateen.delivery.domain.store.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCreateRequest {

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String city;

    @NotBlank
    private String district;

    @NotBlank
    private String street;

    private String detail; // 상세주소는 선택값

    @NotNull
    private LocalTime openTime;

    @NotNull
    private LocalTime closeTime;

    @Min(0)
    private int estimatedPickupTime;

    @Min(0)
    private int minOrderAmount;

    @Min(0)
    private int deliveryTip;

    @Size(max = 1000)
    private String notice;
}
