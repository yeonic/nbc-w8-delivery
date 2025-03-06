package com.ateen.delivery.domain.review.repository;

import com.ateen.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndOrder(Long id, Order order);

    List<Review> findAllByStoreId(Long storeId);  // 특정 store 모든 리뷰 조회

    List<Review> findAllByStoreIdAndStars(Long storeId, Integer stars);// 특정 store + 특정 별점 조회

    boolean existsByOrder(Order order);
}
