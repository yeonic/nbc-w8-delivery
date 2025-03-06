package com.ateen.delivery.domain.orders.dto.request;

import com.ateen.delivery.domain.orders.constants.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class OrderCreateRequest {

    // TODO : userId, storeId, menuId

    @NotNull(message = "필수 입력 사항입니다.")
    @Min(value = 0, message = "주문 수량은 0 이상이어야 합니다.")
    private Integer amount;

    @NotNull(message = "필수 입력 사항입니다.")
    private OrderType orderType;

    @NotBlank(message = "필수 입력 사항입니다.")
    @Size(max = 10, message = "시/도명은 10자 이하여야 합니다.")
    private String city;

    @NotBlank(message = "필수 입력 사항입니다.")
    @Size(max = 10, message = "시/군/구명은 10자 이하여야 합니다.")
    private String district;

    @NotBlank(message = "필수 입력 사항입니다.")
    @Size(max = 20, message = "도로명은 20자 이하여야 합니다.")
    private String street;

    @Size(max = 40, message = "상세주소는 40자 이하여야 합니다.")
    private String detail;
}
