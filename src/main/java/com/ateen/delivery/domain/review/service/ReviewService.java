package com.ateen.delivery.domain.review.service;

import com.ateen.delivery.domain.review.dto.request.ReviewSaveRequest;
import com.ateen.delivery.domain.review.dto.request.ReviewUpdateRequest;
import com.ateen.delivery.domain.review.dto.response.ReviewResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewSaveResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewUpdateResponse;
import com.ateen.delivery.domain.review.entity.Review;
import com.ateen.delivery.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public ReviewSaveResponse save(Long orderNum, ReviewSaveRequest request) {
        Orders orders = orderRepository.findById(orderNum).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 주문을 찾을 수 없습니다.")
        );
        Review review = new Review(request.getStars(), request.getContent(), findOrder);
        reviewRepository.save(review);

        return new ReviewSaveResponse(review.getId(),
                                      review.getOrders().getId(),
                                      review.getStars(),
                                      review.getContent());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findAll(Long orderNum) {

        Orders order = orderRepository.findById(orderNum).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 주문을 찾을 수 없습니다.")
        );

        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(review -> new ReviewResponse(review.getId(),
                                                           review.getOrders().getId(),
                                                           review.getStars(),
                                                           review.getContent())).toList();
    }

    @Transactional(readOnly = true)
    public ReviewResponse findOne(Long orderNum, Long reviewId) {

        Orders order = orderRepository.findById(orderNum).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 주문을 찾을 수 없습니다.")
        );

        Review review = reviewRepository.findByIdAndOrders(reviewId, order).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다.")
        );

        return new ReviewResponse(review.getId(), review.getOrders().getId(), review.getStars(), review.getContent());
    }

    @Transactional
    public ReviewUpdateResponse update(Long userId, Long orderNum, Long reviewId, ReviewUpdateRequest request) {

        Orders order = orderRepository.findById(orderNum).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 주문을 찾을 수 없습니다.")
        );

        //주문의 작성자가 없을 때 발생하는 예외.
        if (order.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문의 작성자를 찾을 수 없습니다.");
        }

        if (!order.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 주문을 수정할 권한이 없습니다.");
        }

        Review review = reviewRepository.findByIdAndOrders(reviewId, order).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다.")
        );

        review.update(request.getStars(), request.getContent());

        return new ReviewUpdateResponse(review.getId(),
                                        review.getOrders().getId(),
                                        review.getStars(),
                                        review.getContent());
    }

    @Transactional
    public void delete(Long userId, Long orderId, Long reviewId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 주문을 찾을 수 없습니다.")
        );

        if (order.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문의 작성자를 찾을 수 없습니다.");
        }

        if (!order.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 주문을 삭제할 권한이 없습니다.");
        }

        Review review = reviewRepository.findByIdAndOrders(reviewId, order).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다.")
        );

        reviewRepository.deleteById(review.getId());
    }

}
