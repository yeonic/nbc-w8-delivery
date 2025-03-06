package com.ateen.delivery.domain.store.service;

import com.ateen.delivery.domain.common.exception.ForbiddenAccessException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.store.dto.request.StoreCreateRequest;
import com.ateen.delivery.domain.store.dto.request.StoreRequest;
import com.ateen.delivery.domain.store.dto.request.StoreUpdateRequest;
import com.ateen.delivery.domain.store.dto.response.StoreResponse;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.dto.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    private User owner;
    private Store store;

    @BeforeEach
    void setUp() {

        owner = User.builder()
                .email("owner@example.com")
                .password("password123")
                .name("사장님")
                .nickname("ownerNick")
                .phoneNum("010-1234-5678")
                .userType(UserType.OWNER)
                .city("서울")
                .district("강남구")
                .street("테헤란로")
                .detail("100번지")
                .build();

        ReflectionTestUtils.setField(owner, "id", 1L);

        store = Store.builder()
                .owner(owner)
                .name("테스트 가게")
                .phoneNumber("010-5555-6666")
                .address(new Address(
                        "서울",
                        "강남구",
                        "테헤란로",
                        "100번지"
                ))
                .openTime(LocalTime.of(10, 0))
                .closeTime(LocalTime.of(21, 0))
                .estimatedPickupTime(20)
                .minOrderAmount(15000)
                .deliveryTip(2500)
                .isOpen(true)
                .isDeleted(false)
                .notice("가게 공지")
                .build();
    }

    @Test
    void 가게_생성_성공() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));

        // given
        Long ownerId = 1L;
        StoreCreateRequest request = new StoreCreateRequest(
                "새로운 가게", "010-5555-6666",
                "서울", "강남구", "역삼로", "99",
                LocalTime.of(10, 0), LocalTime.of(21, 0),
                20, 15000, 2500, "가게 공지"
        );

        // when
        Response<StoreResponse> responseEntity = storeService.createStore(request, ownerId);
        StoreResponse response = responseEntity.getData();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(request.getName());
    }

    @Test
    void 가게_단건조회_성공() {
        // given
        Long storeId = 1L;

        when(storeRepository.findByIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));

        // when
        Response<StoreResponse> responseEntity = storeService.findStoreById(storeId);
        StoreResponse response = responseEntity.getData();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(store.getName());
    }

    @Test
    void 가게_수정_성공() {
        // given
        Long storeId = 1L;
        Long ownerId = 1L;

        StoreRequest request = new StoreRequest(
                "수정된 가게", "010-7777-8888",
                "인천", "응남구", "응삼로", "99",
                LocalTime.of(9, 0), LocalTime.of(22, 0),
                15, 12000, 2000, "수정된 공지"
        );

        when(storeRepository.findByIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));

        // when
        Response<StoreResponse> responseEntity = storeService.updateStore(storeId, request, ownerId);
        StoreResponse response = responseEntity.getData();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(request.getName());
    }

    @Test
    void 가게_삭제_성공() {
        // given
        Long storeId = 1L;
        Long ownerId = 1L;

        Store store = mock(Store.class); // store 객체를 Mock으로 생성
        when(storeRepository.findByIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));

        // when
        storeService.deleteStore(storeId, ownerId);

        // then
        verify(store).setDeleted(true);
    }

    @Test
    void 가게_삭제_실패_권한없음() {
        // given
        Long storeId = 1L;
        Long anotherUserId = 999L; // 다른 유저 ID

        when(storeRepository.findByIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));

        // when & then
        assertThatThrownBy(() -> storeService.deleteStore(storeId, anotherUserId))
                .isInstanceOf(ForbiddenAccessException.class)
                .hasMessage("해당 가게를 삭제할 권한이 없습니다.");
    }
}
