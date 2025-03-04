package com.ateen.delivery.domain.review.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewSaveResponse {
    private final Long id;
    private final Long orderId;
    private final Integer stars;
    private final String content;
}
