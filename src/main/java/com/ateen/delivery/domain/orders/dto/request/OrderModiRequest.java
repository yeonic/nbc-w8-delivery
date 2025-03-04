package com.ateen.delivery.domain.orders.dto.request;

import com.ateen.delivery.domain.orders.constants.OrderType;
import lombok.Getter;

@Getter
public class OrderModiRequest {

    private OrderType orderType;
    private Integer amount;
}
