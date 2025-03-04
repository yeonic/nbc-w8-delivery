package com.ateen.delivery.domain.menu.dto.request;

import lombok.Getter;

@Getter
public class MenuSaveRequest {
    private String name;
    private Integer price;
    private String detail;
}
