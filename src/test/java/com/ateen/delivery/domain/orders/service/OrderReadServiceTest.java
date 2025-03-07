package com.ateen.delivery.domain.orders.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.menu.entity.Menu;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.orders.dto.response.OrderResponse;
import com.ateen.delivery.domain.orders.dto.response.OrderStatusResponse;
import com.ateen.delivery.domain.orders.entity.Order;
import com.ateen.delivery.domain.orders.repository.OrderRepository;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.global.dto.error.ErrorCode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderReadServiceTest {

    private User owner;
    private User customer;
    private Store store;
    private Menu menu;
    private Order order;

    @Mock
    OrderRepository repository;

    @InjectMocks
    OrderReadService service;

    @BeforeEach
    void before() {
        owner = User.builder()
                .email("user@example.com")
                .password("securepassword")
                .name("홍길동")
                .phoneNum("010-1234-5678")
                .address(new Address("서울시", "강남구", "테헤란로 123", "빌딩 5층"))
                .nickname("길동이")
                .birthDay(LocalDate.of(1990, 5, 20))
                .userType(UserType.OWNER)
                .build();

        customer = User.builder()
                .email("use2r@example.com")
                .password("securepassword")
                .name("홍길동1")
                .phoneNum("010-1234-5678")
                .address(new Address("서울시", "강남구", "테헤란로 13", "빌딩 5층"))
                .nickname("길동이1")
                .birthDay(LocalDate.of(1990, 5, 20))
                .userType(UserType.USER)
                .build();

        // 가짜 영업시간 리스트 생성
        List<StoreBusinessHour> businessHours = new ArrayList<>();
        businessHours.add(StoreBusinessHour.builder().store(null)
                .dayOfWeek(DayOfWeek.SUNDAY).openTime(LocalTime.NOON)
                .closeTime(LocalTime.MIDNIGHT).isOpen(true).build());

        businessHours.add(StoreBusinessHour.builder().store(null)
                .dayOfWeek(DayOfWeek.THURSDAY).openTime(LocalTime.NOON)
                .closeTime(LocalTime.MIDNIGHT).isOpen(true).build());

        store = Store.builder()
                .id(1L)
                .owner(owner)
                .name("My Store")
                .phoneNumber("010-1234-5678")
                .address(new Address("서울시", "서초구", "테헤란로 123", "빌딩 5층"))
                .notice("오전 9시부터 밤 10시까지 영업합니다.")
                .estimatedPickupTime(30)
                .minOrderAmount(15000)
                .deliveryTip(3000)
                .isOpen(true)
                .businessHours(businessHours)
                .build();
        
        menu = new Menu("메뉴1", 17000, "바보", store);
        ReflectionTestUtils.setField(menu, "id", 1L);

        order = Order.createOrder(
                OrderType.DELIVERY,
                customer.getAddress(),
                12,
                3000,
                LocalDateTime.now(),
                store,
                customer,
                menu
        );
        ReflectionTestUtils.setField(order, "id", "abcd");
    }

    @Test
    void 주문_단건_조회_성공() {
        // given
        given(repository.findByIdCreatedByOrOwnedByUser(anyString(), anyLong()))
                .willReturn(Optional.of(order));
        // when
        OrderResponse orderResponse = service.findOrder(anyString(), anyLong());

        // then
        assertThat(orderResponse.getOrderId()).isEqualTo(order.getId());
    }

    @Test
    void 주문_단건_조회_실패() {
        // given
        given(repository.findByIdCreatedByOrOwnedByUser(anyString(), anyLong()))
                .willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> service.findOrder(anyString(), anyLong()))
                .isInstanceOf(ClientException.class)
                .satisfies(exception -> {
                    ClientException clientException = (ClientException) exception;
                    assertThat(clientException.getErrorCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND);
                });
    }

    @Test
    void 주문_상태_조회_성공() {
        // given
        given(repository.findByIdCreatedByOrOwnedByUser(anyString(), anyLong()))
                .willReturn(Optional.of(order));
        // when
        OrderStatusResponse orderStatus = service.findOrderStatus(anyString(), anyLong());

        // then
        assertThat(orderStatus.getOrderId()).isEqualTo(order.getId());
    }

    @Test
    void 주문_상태_조회_실패() {
        // given
        given(repository.findByIdCreatedByOrOwnedByUser(anyString(), anyLong()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> service.findOrderStatus(anyString(), anyLong()))
                .isInstanceOf(ClientException.class)
                .satisfies(exception -> {
                    ClientException clientException = (ClientException) exception;
                    assertThat(clientException.getErrorCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND);
                });
    }
}