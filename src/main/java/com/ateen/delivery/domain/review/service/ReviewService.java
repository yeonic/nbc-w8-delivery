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

    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewSaveResponse save(Long storeId, Long orderId, ReviewSaveRequest request) {

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다.")
        );

        //동일한 id로 작성된 리뷰인지 검증
        if (reviewRepository.existsByOrder(order)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 해당 주문에 대한 리뷰가 존재합니다.");
        }

        Review review = new Review(request.getStars(), request.getContent(), order, store);
        reviewRepository.save(review);

        return new ReviewSaveResponse(review.getId(),
                                      store.getId(),
                                      order.getId(),
                                      review.getStars(),
                                      review.getContent(),
                                      review.getCreatedAt(),
                                      review.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findAll(Long storeId, Integer stars) {

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        List<Review> reviews = (stars != null) ?
                                reviewRepository.findByStoreIdAndStars(storeId, stars) :
                                reviewRepository.findByStoreId(storeId);

        return reviews.stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        store.getId(),
                        review.getOrder().getId(),
                        review.getStars(),
                        review.getContent(),
                        review.getCreatedAt(),
                        review.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public ReviewUpdateResponse update(Long userId, Long storeId, Long orderId, Long reviewId, ReviewUpdateRequest request) {

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다.")
        );

        authByUserIdAndOrder(userId, order, store);

        Review review = reviewRepository.findByIdAndOrder(reviewId, order).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다.")
        );

        review.update(request.getStars(), request.getContent());

        return new ReviewUpdateResponse(review.getId(),
                                        store.getId(),
                                        order.getId(),
                                        review.getStars(),
                                        review.getContent(),
                                        review.getCreatedAt(),
                                        review.getUpdatedAt());
    }

    @Transactional
    public void delete(Long userId, Long storeId, Long orderId, Long reviewId) {

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다.")
        );

        authByUserIdAndOrder(userId, order, store);

        Review review = reviewRepository.findByIdAndOrder(reviewId, order).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다.")
        );

        reviewRepository.delete(review);
    }

    //예외 검증 로직
    private static void authByUserIdAndOrder(Long userId, Order order, Store store) {
        if (!order.getStore().getId().equals(store.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 주문은 지정된 가게에 속하지 않습니다.");
        }

        if (order.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "주문한 유저를 찾을 수 없습니다.");
        }

        if (!order.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 주문을 수정할 권한이 없습니다.");
        }
    }

}
