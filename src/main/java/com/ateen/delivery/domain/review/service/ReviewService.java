package com.ateen.delivery.domain.review.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.review.dto.request.ReviewSaveRequest;
import com.ateen.delivery.domain.review.dto.request.ReviewUpdateRequest;
import com.ateen.delivery.domain.review.dto.response.ReviewResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewSaveResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewUpdateResponse;
import com.ateen.delivery.domain.review.entity.Review;
import com.ateen.delivery.domain.review.repository.ReviewRepository;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.dto.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewSaveResponse save(Long storeId, Long userId, ReviewSaveRequest request) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ClientException(ErrorCode.STORE_NOT_FOUND)
        );

        Review review = new Review(request.getStars(), request.getContent(), store);
        reviewRepository.save(review);

        return new ReviewSaveResponse(review.getId(),
                store.getId(),
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
                        review.getStars(),
                        review.getContent(),
                        review.getCreatedAt(),
                        review.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public ReviewUpdateResponse update(Long userId, Long storeId, Long reviewId, ReviewUpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ClientException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getStore().getId().equals(storeId)) {
            throw new ClientException(ErrorCode.STORE_NOT_FOUND);
        }

        review.update(request.getStars(), request.getContent());

        return new ReviewUpdateResponse(review.getId(),
                review.getStore().getId(),
                review.getStars(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt());
    }

    @Transactional
    public void delete(Long userId, Long storeId, Long reviewId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ClientException(ErrorCode.STORE_NOT_FOUND)
        );

        Review review = reviewRepository.findByIdAndStore(reviewId, store)
                .orElseThrow(() -> new ClientException(ErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
    }

}
