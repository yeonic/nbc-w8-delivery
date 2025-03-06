package com.ateen.delivery.domain.orders.dto.request;

import com.ateen.delivery.domain.orders.constants.OrderType;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class OrderModiRequest {

    private OrderType orderType;

    @Min(value = 0, message = "주문 수량은 0 이상이어야 합니다.")
    private Integer amount;
}
