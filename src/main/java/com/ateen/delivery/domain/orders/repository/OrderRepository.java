package com.ateen.delivery.domain.orders.repository;

import com.ateen.delivery.domain.orders.entity.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findAll(Pageable pageRequest);

    Optional<Order> findByIdAndStoreId(String orderId, Long storeId);
}
