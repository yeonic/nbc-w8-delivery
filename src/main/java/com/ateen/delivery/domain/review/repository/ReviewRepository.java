package com.ateen.delivery.domain.review.repository;

import com.ateen.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndOrders(Long id, Orders orders);
}
