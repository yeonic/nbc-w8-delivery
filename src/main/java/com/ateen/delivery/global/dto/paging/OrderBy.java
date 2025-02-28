package com.ateen.delivery.global.dto.paging;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderBy {
    LIKES("likes"),
    UPDATED_AT("updatedAt"),
    CREATED_AT("createdAt");

    private final String value;

    public static OrderBy ofString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value는 null이 아니어야 합니다.");
        }
        return Arrays.stream(OrderBy.values())
            .filter(orderBy -> orderBy.getValue().equalsIgnoreCase(value))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("잘못된 value입니다."));
    }
}
