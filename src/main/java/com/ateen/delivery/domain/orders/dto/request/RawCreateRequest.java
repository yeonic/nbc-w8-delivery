package com.ateen.delivery.domain.orders.dto.request;

import lombok.Getter;

@Getter
public class RawCreateRequest {

    // TODO : userId, storeId, menuId
    private Integer amount;
    private String orderType;
    private String city;
    private String district;
    private String street;
    private String detail;
}
