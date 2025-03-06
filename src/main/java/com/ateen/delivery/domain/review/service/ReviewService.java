package com.ateen.delivery.domain.review.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.orders.entity.Order;
import com.ateen.delivery.domain.orders.repository.OrderRepository;
import com.ateen.delivery.domain.review.dto.request.ReviewSaveRequest;
import com.ateen.delivery.domain.review.dto.request.ReviewUpdateRequest;
import com.ateen.delivery.domain.review.dto.response.ReviewResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewSaveResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewUpdateResponse;
import com.ateen.delivery.domain.review.entity.Review;
import com.ateen.delivery.domain.review.repository.ReviewRepository;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.global.dto.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewSaveResponse save(Long storeId, String orderId, ReviewSaveRequest request) {

        //특정 가게의 주문을 찾는 메서드 (튜터님 피드백)
        Order order = orderRepository.findByIdAndStoreId(orderId, storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        //동일한 주문에 작성한 리뷰인지 검증
        if (reviewRepository.existsByOrder(order)) {
            throw new ClientException(ErrorCode.ORDER_IS_ALIVE);
        }

        Review review = new Review(request.getStars(), request.getContent(), order);
        reviewRepository.save(review);

        return new ReviewSaveResponse(review.getId(),
                order.getStore().getId(),
                order.getId(),
                review.getStars(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findAll(Long storeId, Integer stars) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        List<Review> reviews = stars != null ?
                reviewRepository.findAllByStoreIdAndStars(storeId, stars) :
                reviewRepository.findAllByStoreId(storeId);

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
    public ReviewUpdateResponse update(Long userId, Long storeId, String orderId, Long reviewId,
            ReviewUpdateRequest request) {

        Order order = orderRepository.findByIdAndStoreId(orderId, storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        authByUserIdAndOrder(userId, order);

        Review review = reviewRepository.findByIdAndOrder(reviewId, order)
                .orElseThrow(() -> new ClientException(ErrorCode.REVIEW_NOT_FOUND));

        review.update(request.getStars(), request.getContent());

        return new ReviewUpdateResponse(review.getId(),
                order.getStore().getId(),
                order.getId(),
                review.getStars(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt());
    }

    @Transactional
    public void delete(Long userId, Long storeId, String orderId, Long reviewId) {

        Order order = orderRepository.findByIdAndStoreId(orderId, storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        authByUserIdAndOrder(userId, order);

        Review review = reviewRepository.findByIdAndOrder(reviewId, order)
                .orElseThrow(() -> new ClientException(ErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
    }

    //예외 검증 로직
    private void authByUserIdAndOrder(Long userId, Order order) {

        // 에러가 난다면 이미 NullPointerException이 getId를 호출하는 시점에 발생하는 코드
//        if (order.getUser().getId() == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "주문한 유저를 찾을 수 없습니다.");
//        }

        if (!order.getUser().getId().equals(userId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }
    }

}
