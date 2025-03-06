package com.ateen.delivery.domain.orders.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.orders.dto.response.OrderResponse;
import com.ateen.delivery.domain.orders.dto.response.OrderStatusResponse;
import com.ateen.delivery.domain.orders.entity.Order;
import com.ateen.delivery.domain.orders.repository.OrderRepository;
import com.ateen.delivery.global.dto.error.ErrorCode;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderReadService {

    private final OrderRepository repository;

    public Page<OrderResponse> findAll(PagingCondition pagingCondition, Long userId) {
        Pageable pageRequest = PagingCondition.toPageRequest(pagingCondition);

        return repository.findAllCreatedByOrOwnedByUser(userId, pageRequest)
                .map(OrderResponse::fromOrder);
    }

    public OrderResponse findOrder(String orderNum, Long userId) {
        Order findOrder = repository.findByIdCreatedByOrOwnedByUser(orderNum, userId)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));
        return OrderResponse.fromOrder(findOrder);
    }

    public OrderStatusResponse getOrderStatus(String orderNum, Long userId) {
        Order findOrder = repository.findByIdCreatedByOrOwnedByUser(orderNum, userId)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        return OrderStatusResponse.fromOrders(findOrder);
    }

}
