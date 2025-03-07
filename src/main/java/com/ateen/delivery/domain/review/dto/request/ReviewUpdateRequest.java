package com.ateen.delivery.domain.review.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewUpdateRequest {

    @NotNull(message = "별점은 최소 1점입니다.")
    @Min(value = 1, message = "별점은 최소 1점 이상이어야 합니다.")
    private Integer stars;

    @NotBlank(message = "리뷰는 최소 5자입니다.")
    @Size(min = 5, max = 1000)
    private String content;
}
