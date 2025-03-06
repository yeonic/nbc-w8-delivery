package com.ateen.delivery.domain.orders.service;

import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.menu.entity.Menu;
import com.ateen.delivery.domain.menu.repository.MenuRepository;
import com.ateen.delivery.domain.orders.constants.DeliveryStatus;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.orders.dto.request.OrderCreateRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderModiRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderStatusModiRequest;
import com.ateen.delivery.domain.orders.dto.response.OrderResponse;
import com.ateen.delivery.domain.orders.dto.response.OrderStatusResponse;
import com.ateen.delivery.domain.orders.entity.Order;
import com.ateen.delivery.domain.orders.repository.OrderRepository;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.dto.error.ErrorCode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderWriteService {

    private final OrderRepository repository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    public OrderResponse save(
            OrderCreateRequest request, LocalDateTime createdTime, Long currentUserId
    ) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findByIdWithBusinessHours(request.getStoreId())
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        // 자신의 가게에 주문하는 경우
        if (user.getId().equals(store.getOwner().getId())) {
            throw new ClientException(ErrorCode.CANNOT_JAJEON_GEORAE);
        }

        Menu menu = menuRepository.findByIdAndStoreAndIsDeleted(request.getMenuId(), store, 0)
                .orElseThrow(() -> new ClientException(ErrorCode.MENU_NOT_FOUND));

        // 최소 주문 금액 예외처리
        if (menu.getPrice() * request.getAmount() < store.getMinOrderAmount()) {
            throw new ClientException(ErrorCode.MIN_PRICE_NOT_FULFILLED);
        }

        // 영업 시간이 아닐 경우
        List<StoreBusinessHour> businessHours = store.getBusinessHours();
        checkStoreIsOpen(createdTime, businessHours);

        int deliveryFee = request.getOrderType() == OrderType.DELIVERY ? store.getDeliveryTip() : 0;

        Address targetAddress =
                new Address(request.getCity(), request.getDistrict(), request.getStreet(), request.getDetail());

        Order newOrder = Order.createOrder(
                request.getOrderType(), targetAddress, request.getAmount(), deliveryFee, createdTime,
                store, user, menu
        );
        return OrderResponse.fromOrder(repository.save(newOrder));
    }


    public OrderResponse updateOrder(
            String orderNum, OrderModiRequest request, Long currentUserId
    ) {
        Order findOrder = repository.findByIdAndUserWithStore(orderNum, currentUserId)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        if (findOrder.getOrderStatus() != OrderStatus.PENDING) {
            throw new ClientException(ErrorCode.ORDER_NOT_MUTABLE);
        }

        //  배송 타입 변경 -> 주소도 같이 변경해줘야 함.
        if (findOrder.getOrderType() != request.getOrderType()) {
            // Delivery로 변경
            if (request.getOrderType() == OrderType.DELIVERY) {
                findOrder.updateAddress(findOrder.getUser().getAddress());
            } else {
                findOrder.updateAddress(findOrder.getStore().getAddress());
            }
        }

        updateOrder(request, findOrder);
        return OrderResponse.fromOrder(findOrder);
    }

    public OrderResponse cancelOrder(String orderNum, Long currentUserId) {
        Order findOrder = repository.findByIdCreatedByOrOwnedByUser(orderNum, currentUserId)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        if (findOrder.getOrderStatus() != OrderStatus.PENDING) {
            throw new ClientException(ErrorCode.ORDER_NOT_MUTABLE);
        }

        findOrder.updateOrderStatus(OrderStatus.CANCEL);
        return OrderResponse.fromOrder(findOrder);
    }

    public OrderStatusResponse updateOrderStatus(
            String orderNum, OrderStatusModiRequest request, LocalDateTime modifiedTime, AuthUser currentUser
    ) {
        // OWNER가 아닌 사용자가 수정을 시도할 경우
        if (currentUser.getUserType() != UserType.OWNER) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        Order findOrder = repository.findByIdCreatedByOrOwnedByUser(orderNum, currentUser.getId())
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        OrderStatus orderReq = nullSafeValue(request.getOrderStatus(), findOrder.getOrderStatus());
        DeliveryStatus deliveryReq = nullSafeValue(request.getDeliveryStatus(), findOrder.getDeliveryStatus());

        // 배달 종료 시나리오
        if (deliveryReq == DeliveryStatus.DONE) {
            // 현재 주문이 배달인 경우가 아니면 예외 발생
            if (findOrder.getOrderType() != OrderType.DELIVERY) {
                throw new ClientException(ErrorCode.NOT_DELIVERY);
            }
            // 출발하지 않은 주문 배달 종료 처리하면 예외 발생
            if (findOrder.getOrderStatus() != OrderStatus.DEPART) {
                throw new ClientException(ErrorCode.NOT_DEPARTED);
            }

            findOrder.updateDeliveryStatus(deliveryReq);
            findOrder.updateDeliveryDoneAt(modifiedTime);
            return OrderStatusResponse.fromOrders(findOrder);
        }

        // 배송 출발 시나리오
        if (orderReq == OrderStatus.DEPART) {
            // 배달인 경우 -> DeliveryStatus를 ONROAD로 변경
            if (findOrder.getOrderType() == OrderType.DELIVERY) {
                findOrder.updateDeliveryStatus(DeliveryStatus.ONROAD);
                findOrder.updatePickupAt(modifiedTime);
                findOrder.updateOrderStatus(orderReq);
                return OrderStatusResponse.fromOrders(findOrder);
            }

            // 배달이 아닌 경우 -> OrderStatus:DEPART, DeliveryStatus:DONE
            findOrder.updateDeliveryStatus(DeliveryStatus.DONE);
            findOrder.updatePickupAt(modifiedTime);
            findOrder.updateDeliveryDoneAt(modifiedTime);
            findOrder.updateOrderStatus(orderReq);
            return OrderStatusResponse.fromOrders(findOrder);
        }

        // Default case -> 둘 다 업데이트
        findOrder.updateOrderStatus(orderReq);
        findOrder.updateDeliveryStatus(deliveryReq);
        return OrderStatusResponse.fromOrders(findOrder);
    }


    public void delete(String orderNum, Long currentUserId) {
        Order findOrder = repository.findByIdCreatedByOrOwnedByUser(orderNum, currentUserId)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        if (findOrder.getDeliveryStatus() != DeliveryStatus.DONE && findOrder.getOrderStatus() != OrderStatus.CANCEL) {
            throw new ClientException(ErrorCode.ORDER_IS_ALIVE);
        }

        repository.delete(findOrder);
    }

    private <T> T nullSafeValue(T requestValue, T originalValue) {
        return requestValue == null ? originalValue : requestValue;
    }

    private void updateOrder(OrderModiRequest request, Order findOrder) {
        OrderType newOrderType = nullSafeValue(request.getOrderType(), findOrder.getOrderType());
        Integer amount = nullSafeValue(request.getAmount(), findOrder.getAmount());
        findOrder.updateOrder(newOrderType, amount);
    }


    private void checkStoreIsOpen(LocalDateTime createdTime, List<StoreBusinessHour> businessHours) {
        for (StoreBusinessHour bh : businessHours) {
            LocalTime requestTime = createdTime.toLocalTime();
            if (bh.getDayOfWeek() == createdTime.getDayOfWeek() &&
                    (!bh.getOpenTime().isBefore(requestTime) || !requestTime.isBefore(bh.getCloseTime()))
            ) {
                throw new ClientException(ErrorCode.NOT_BUSINESS_HOUR);
            }
        }
    }

}
