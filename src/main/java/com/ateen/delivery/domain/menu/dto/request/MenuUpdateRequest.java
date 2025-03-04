package com.ateen.delivery.domain.menu.dto.request;

import lombok.Getter;

@Getter
public class MenuUpdateRequest {
    private String name;
    private Integer price;
    private String detail;
}
