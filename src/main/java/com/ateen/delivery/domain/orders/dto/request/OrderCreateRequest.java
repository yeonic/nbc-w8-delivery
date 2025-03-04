package com.ateen.delivery.domain.orders.dto.request;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.OrderType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreateRequest {

    // TODO : userId, storeId, menuId
    private Integer amount;
    private OrderType orderType;
    private Address targetAddress;

    @Builder
    public OrderCreateRequest(Integer amount, OrderType orderType, Address targetAddress) {
        this.amount = amount;
        this.orderType = orderType;
        this.targetAddress = targetAddress;
    }
}
