package com.ateen.delivery.domain.menu.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MenuSaveResponse {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String detail;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
