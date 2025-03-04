package com.ateen.delivery.domain.review.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewResponse {
    private final Long id;
    private final Long orderId; //주문 ID
    private final Integer stars;
    private final String content;
}
