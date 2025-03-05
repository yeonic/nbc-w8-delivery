package com.ateen.delivery.domain.review.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewResponse {
    private final Long id;
    private final Long storeId;
    private final Long orderId;
    private final Integer stars;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
