package com.ateen.delivery.domain.review.dto.request;

import lombok.Getter;

@Getter
public class ReviewSaveRequest {
    private Integer stars;
    private String content;
}
