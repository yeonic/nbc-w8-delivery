package com.ateen.delivery.domain.orders.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.menu.entity.Menu;
import com.ateen.delivery.domain.menu.repository.MenuRepository;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.orders.dto.request.OrderCreateRequest;
import com.ateen.delivery.domain.orders.entity.Order;
import com.ateen.delivery.domain.orders.repository.OrderRepository;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderWriteServiceTest {

    private User owner;
    private User customer;
    private Store store;
    private Menu menu;
    private Order order;

    @Mock private OrderRepository orderRepository;
    @Mock private StoreRepository storeRepository;
    @Mock private UserRepository userRepository;
    @Mock private MenuRepository menuRepository;

    @InjectMocks private OrderWriteService service;

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
        ReflectionTestUtils.setField(owner, "id", 1L);

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
        ReflectionTestUtils.setField(customer, "id", 2L);

        // 가짜 영업시간 리스트 생성
        List<StoreBusinessHour> businessHours = new ArrayList<>();
        businessHours.add(StoreBusinessHour.create(null, DayOfWeek.SUNDAY, LocalTime.NOON, LocalTime.MIDNIGHT, true));
        businessHours.add(StoreBusinessHour.create(null, DayOfWeek.THURSDAY, LocalTime.NOON, LocalTime.MIDNIGHT, true));

        // createStore 메서드 호출
        store = Store.createStore(
                owner,                      // 가게 주인
                "My Store",                 // 가게 이름
                "010-1234-5678",            // 전화번호
                new Address("서울시", "서초구", "테헤란로 123", "빌딩 5층"),                    // 주소
                "오전 9시부터 밤 10시까지 영업합니다.", // 공지사항
                30,                         // 예상 픽업 시간 (분 단위)
                15000,                      // 최소 주문 금액 (예: 15000원)
                3000,                       // 배달비 (예: 3000원)
                true,                       // 현재 영업 중 여부
                businessHours               // 영업시간 리스트
        );
        ReflectionTestUtils.setField(store, "id", 1L);

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
    void 주문_생성_중_유저를_찾을_수_없음() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(
                () -> service.save(any(OrderCreateRequest.class), any(LocalDateTime.class), anyLong()))
                .isInstanceOf(ClientException.class)
                .satisfies(exception -> {
                    ClientException clientException = (ClientException) exception;
                    assertThat(clientException.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
                });
    }

    @Test
    void 주문_생성_중_가게를_찾을_수_없음() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(customer));
        given(storeRepository.findByIdWithBusinessHours(anyLong())).willReturn(Optional.empty());
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        ReflectionTestUtils.setField(orderCreateRequest, "storeId", store.getId());

        // when
        // then
        assertThatThrownBy(
                () -> service.save(any(OrderCreateRequest.class), any(LocalDateTime.class), anyLong()))
                .isInstanceOf(ClientException.class)
                .satisfies(exception -> {
                    ClientException clientException = (ClientException) exception;
                    assertThat(clientException.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
                });
    }

}