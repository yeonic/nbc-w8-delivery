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

    // TODO : TEMP_DELIVERY_FEE는 Store에서 가져오는 deliveryFee로 대체
    private final OrderRepository repository;

    public Page<OrderResponse> findAll(PagingCondition pagingCondition) {
        Pageable pageRequest = PagingCondition.toPageRequest(pagingCondition);
        /** TODO
         * user가 생성했거나, user의 가게에 들어온 주문을 찾는다.
         * pagination을 적용하여 반환한다.
         */
        return repository.findAll(pageRequest).map(OrderResponse::fromOrder);
    }

    public OrderResponse findOrder(String orderNum) {
        // TODO : user가 생성했거나, user의 가게에 들어온 주문을 찾도록 조건 변경

        Order findOrder = repository.findById(orderNum)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));
        return OrderResponse.fromOrder(findOrder);
    }

    public OrderStatusResponse getOrderStatus(String orderNum) {
        // TODO : 주문 당사자들 이외의 사용자가 접근을 시도하는지

        Order findOrder = repository.findById(orderNum)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));
        
        return OrderStatusResponse.fromOrders(findOrder);
    }

}
