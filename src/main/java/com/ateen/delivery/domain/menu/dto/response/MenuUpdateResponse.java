package com.ateen.delivery.domain.menu.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuUpdateResponse {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String detail;
}
