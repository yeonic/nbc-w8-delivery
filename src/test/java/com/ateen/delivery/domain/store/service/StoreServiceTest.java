package com.ateen.delivery.domain.store.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.review.entity.Review;
import com.ateen.delivery.domain.review.repository.ReviewRepository;
import com.ateen.delivery.domain.store.dto.request.StoreBusinessHourRequest;
import com.ateen.delivery.domain.store.dto.request.StoreRequest;
import com.ateen.delivery.domain.store.dto.response.StoreResponse;
import com.ateen.delivery.domain.store.entity.category.*;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.entity.holiday.StoreHoliday;
import com.ateen.delivery.domain.store.repository.StoreHolidayRepository;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.dto.error.ErrorCode;
import com.ateen.delivery.global.dto.paging.OrderBy;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreHolidayRepository storeHolidayRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private StoreService storeService;

    private User owner;
    private Store store;
    private StoreRequest storeRequest;

    @BeforeEach
    void setUp() {
        // Owner 초기화
        owner = User.builder()
                .name("owner")
                .email("owner@email.com")
                .password("password")
                .address(new Address("서울", "강남구", "테헤란로", "100번지")) // ✅ Address 추가
                .userType(UserType.OWNER)
                .build();

        // ID를 수동으로 설정
        ReflectionTestUtils.setField(owner, "id", -1L);

        // 비즈니스 시간 생성
        List<StoreBusinessHourRequest> businessHours = List.of(
                new StoreBusinessHourRequest(DayOfWeek.MONDAY, LocalTime.parse("09:00"), LocalTime.parse("18:00"), true),
                new StoreBusinessHourRequest(DayOfWeek.TUESDAY, LocalTime.parse("09:00"), LocalTime.parse("18:00"), true),
                new StoreBusinessHourRequest(DayOfWeek.WEDNESDAY, LocalTime.parse("09:00"), LocalTime.parse("18:00"), true),
                new StoreBusinessHourRequest(DayOfWeek.THURSDAY, LocalTime.parse("09:00"), LocalTime.parse("18:00"), true),
                new StoreBusinessHourRequest(DayOfWeek.FRIDAY, LocalTime.parse("09:00"), LocalTime.parse("18:00"), true),
                new StoreBusinessHourRequest(DayOfWeek.SATURDAY, LocalTime.parse("10:00"), LocalTime.parse("15:00"), true),
                new StoreBusinessHourRequest(DayOfWeek.SUNDAY, LocalTime.parse("10:00"), LocalTime.parse("15:00"), true)
        );

        // Store 객체 초기화
        store = Store.createStore(
                owner,
                "맛있는 식당",
                "010-1234-5678",
                new Address("서울", "강남구", "테헤란로", "100번지"),
                "오전 10시부터 운영합니다.",
                "1111111111",
                20,
                15000,
                2500,
                true,
                List.of(),
                List.of(StoreCategoryType.일식, StoreCategoryType.양식)
        );

        // StoreRequest 객체 초기화
        storeRequest = new StoreRequest(
                "맛있는 식당",
                "010-1234-5678",
                "1234567890",
                List.of(StoreCategoryType.일식, StoreCategoryType.양식),
                "서울",
                "강남구",
                "테헤란로",
                "100번지",
                20,
                15000,
                2500,
                true,
                "오전 10시부터 운영합니다.",
                businessHours // 비즈니스 시간 추가
        );
    }

    @Test
    void 가게_생성_성공() {
        // Given
        when(userRepository.findById(any())).thenReturn(Optional.of(owner));  // Owner 반환
        when(storeRepository.countByOwnerIdAndIsDeletedFalse(any())).thenReturn(1L);  // 가게가 없는 경우
        when(storeRepository.existsByBusinessNumber(any())).thenReturn(false);  // 사업자 등록번호 중복 없으면
        when(storeRepository.save(any())).thenReturn(store);  // store 저장

        // When
        StoreResponse response = storeService.createStore(storeRequest, owner.getId()).getData();

        // Then
        assertThat(response.getName()).isEqualTo("맛있는 식당");
        verify(storeRepository, times(1)).save(any());
    }

    @Test
    void 가게_중복_사업자번호_예외() {
        // Given
        when(userRepository.findById(any())).thenReturn(Optional.of(owner));  // Owner가 존재
        when(storeRepository.existsByBusinessNumber(any())).thenReturn(true);  // 사업자 번호 중복

        // When & Then
        assertThrows(ClientException.class, () -> storeService.createStore(storeRequest, owner.getId()));
    }

    @Test
    void 가게_생성_유효하지_않은_사업자번호_예외() {
        // Given
        ReflectionTestUtils.setField(storeRequest, "businessNumber", "12345");  // 유효하지 않은 사업자 등록번호
        when(userRepository.findById(any())).thenReturn(Optional.of(owner));  // Owner가 존재

        // When & Then
        assertThrows(ClientException.class, () -> storeService.createStore(storeRequest, owner.getId()));
    }

    @Test
    void 가게_생성_소유자_없을_경우_예외() {
        // Given
        when(userRepository.findById(any())).thenReturn(Optional.empty());  // Owner가 없을 경우

        // When & Then
        assertThrows(ClientException.class, () -> storeService.createStore(storeRequest, owner.getId()));
    }

    @Test
    void 가게_생성_최대_가게수_초과_예외() {
        // Given
        when(userRepository.findById(any())).thenReturn(Optional.of(owner));  // Owner가 존재
        when(storeRepository.countByOwnerIdAndIsDeletedFalse(anyLong())).thenReturn(5L);  // 최대 가게 수 초과

        // When & Then
        assertThrows(ClientException.class, () -> storeService.createStore(storeRequest, owner.getId()));
    }


    @Test
    void 가게_검색_성공() {
        // Given
        Page<Store> stores = new PageImpl<>(List.of(store));

        PagingCondition pagingCondition = new PagingCondition("2", "10", "createdAt", "DESC");

        when(storeRepository.findByNameContainingAndIsDeletedFalse(eq("맛있는"), any(Pageable.class)))
                .thenReturn(stores);
        // When
        Page<StoreResponse> response = storeService.searchStores("맛있는", pagingCondition);
        // Then
        assertThat(response.getContent()).hasSize(1);

    }


    @Test
    void 가게_상세조회_성공() {
        // Given
        when(storeRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Optional.of(store));
        when(reviewRepository.findAllByStoreId(any())).thenReturn(List.of());
        when(storeHolidayRepository.findAllByStoreId(any())).thenReturn(List.of());

        // When
        StoreResponse response = storeService.findStoreById(1L).getData();

        // Then
        assertThat(response.getId()).isEqualTo(store.getId());
    }

    @Test
    void 존재하지_않는_가게_상세조회_예외() {
        // Given
        when(storeRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ClientException.class, () -> storeService.findStoreById(1L));
    }

    @Test
    void 가게_삭제_성공() {
        // Given
        when(storeRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Optional.of(store));

        // When
        storeService.deleteStore(1L, owner.getId());

        // Then
        assertThat(store.isDeleted()).isTrue();
    }
}
