package com.ateen.delivery.domain.orders.dto.request;

import com.ateen.delivery.domain.orders.constants.OrderType;
import lombok.Getter;

@Getter
public class OrderCreateRequest {

    // TODO : userId, storeId, menuId
    private Integer amount;
    private OrderType orderType;
    private String city;
    private String district;
    private String street;
    private String detail;
}
