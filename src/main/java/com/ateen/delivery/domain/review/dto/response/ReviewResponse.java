package com.ateen.delivery.domain.review.dto.response;

import com.ateen.delivery.domain.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewResponse {
    private final Long id;
    private final Long storeId;
    private final String orderId;
    private final Integer stars;
    private final String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getStore().getId(),
                review.getOrder().getId(),
                review.getStars(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
