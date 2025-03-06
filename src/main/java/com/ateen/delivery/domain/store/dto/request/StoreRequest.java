package com.ateen.delivery.domain.store.dto.request;

import com.ateen.delivery.domain.common.vo.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequest {

    @NotBlank(message = "가게 이름은 필수 입력입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력입니다.")
    private String phoneNumber;

    @NotBlank(message = "도시는 필수 입력입니다.")
    private String city;

    @NotBlank(message = "구/군은 필수 입력입니다.")
    private String district;

    @NotBlank(message = "도로명은 필수 입력입니다.")
    private String street;

    private String detail;

    @Min(value = 5, message = "예상 픽업 시간은 최소 5분 이상이어야 합니다.")
    private int estimatedPickupTime;

    @Min(value = 1000, message = "최소 주문 금액은 1000원 이상이어야 합니다.")
    private int minOrderAmount;

    @Min(value = 0, message = "배달팁은 0원 이상이어야 합니다.")
    private int deliveryTip;

    private boolean isOpen;

    private String notice;

    @Valid
    @NotEmpty(message = "영업시간(businessHours)은 필수 입력 값이며, 7개 요일을 모두 포함해야 합니다.")
    private List<StoreBusinessHourRequest> businessHours;

    public boolean isValidBusinessHours() {
        return businessHours != null && businessHours.stream().allMatch(StoreBusinessHourRequest::isValid);
    }

    public Address toAddress() {
        return new Address(city, district, street, detail);
    }
}
