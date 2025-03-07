package com.ateen.delivery.domain.orders.repository;

import com.ateen.delivery.domain.orders.entity.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId OR o.store.owner.id = :userId")
    Page<Order> findAllCreatedByOrOwnedByUser(@Param("userId") Long userId, Pageable pageRequest);

    @Query("SELECT o FROM Order o JOIN FETCH o.user "
            + "WHERE o.id = :orderId AND (o.user.id = :userId OR o.store.owner.id = :userId)")
    Optional<Order> findByIdCreatedByOrOwnedByUser(@Param("orderId") String orderId, @Param("userId") Long userId);

    Optional<Order> findByIdAndStoreId(String orderId, Long storeId);


    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.store s JOIN FETCH o.user u "
            + "WHERE o.id = :orderId AND o.user.id = :userId")
    Optional<Order> findByIdAndUserWithStore(@Param("orderId") String orderId, @Param("userId") Long userId);
}
