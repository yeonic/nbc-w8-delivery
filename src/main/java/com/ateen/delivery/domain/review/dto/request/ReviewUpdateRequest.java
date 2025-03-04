package com.ateen.delivery.domain.review.dto.request;

import lombok.Getter;

@Getter
public class ReviewUpdateRequest {
    private Integer stars;
    private String content;
}
