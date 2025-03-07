package com.ateen.delivery.domain.store.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.review.entity.Review;
import com.ateen.delivery.domain.review.repository.ReviewRepository;
import com.ateen.delivery.domain.store.dto.request.StoreBusinessHourRequest;
import com.ateen.delivery.domain.store.dto.request.StoreRequest;
import com.ateen.delivery.domain.store.dto.response.StoreResponse;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import com.ateen.delivery.domain.store.entity.holiday.StoreHoliday;
import com.ateen.delivery.domain.store.repository.StoreHolidayRepository;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.error.ErrorCode;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoreHolidayRepository storeHolidayRepository;
    private final ReviewRepository reviewRepository;

    private static final int MAX_STORES_PER_OWNER = 3;

    @Transactional
    public Response<StoreResponse> createStore(StoreRequest request, Long ownerId) {
        User owner = getValidOwner(ownerId);

        if (storeRepository.countByOwnerIdAndIsDeletedFalse(ownerId) >= MAX_STORES_PER_OWNER) {
            throw new ClientException(ErrorCode.STORE_OVER_CREATION);
        }

        if (!request.getBusinessNumber().matches("\\d{10}")) {
            throw new ClientException(ErrorCode.INVALID_BUSINESS_NUMBER);
        }

        if (storeRepository.existsByBusinessNumber(request.getBusinessNumber())) {
            throw new ClientException(ErrorCode.DUPLICATE_BUSINESS_NUMBER);
        }
        Store store = Store.createStore(
                owner,
                request.getName(),
                request.getPhoneNumber(),
                request.toAddress(),
                request.getNotice(),
                request.getBusinessNumber(),
                request.getEstimatedPickupTime(),
                request.getMinOrderAmount(),
                request.getDeliveryTip(),
                request.getIsOpen(),
                List.of(),
                request.getCategories()
        );

        List<StoreBusinessHour> businessHours = createBusinessHours(request, store);
        store.updateBusinessHours(businessHours);

        storeRepository.save(store);
        return Response.of(StoreResponse.from(store));
    }

    @Transactional
    public Page<StoreResponse> searchStores(String name, PagingCondition pagingCondition) {
        Pageable pageable = PagingCondition.toPageRequest(pagingCondition);

        Page<Store> stores = StringUtils.hasText(name) ?
                storeRepository.findByNameContainingAndIsDeletedFalse(name, pageable) :
                storeRepository.findAllByIsDeletedFalse(pageable);

        return stores.map(StoreResponse::from);
    }

    @Transactional
    public Response<StoreResponse> findStoreById(Long storeId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        List<Review> reviews = reviewRepository.findAllByStoreId(storeId); // ✅ 리뷰 조회 추가
        List<StoreHoliday> holidays = storeHolidayRepository.findAllByStoreId(storeId); // ✅ 휴무일 조회 추가
        return Response.of(StoreResponse.from(store, reviews, holidays)); // ✅ 리뷰 & 휴무일 포함 응답
    }

    @Transactional
    public Response<StoreResponse> updateStore(Long storeId, StoreRequest request, Long ownerId) {
        Store store = getStoreWithPermission(ownerId, storeId);

        store.update(request.getName(), request.getPhoneNumber(), request.toAddress(), request.getNotice());

        if (request.getCategories() != null) {
            store.updateCategories(request.getCategories());
        }

        if (!request.getIsOpen()) {
            store.setOpen(false);
        } else {
            store.setOpen(true);
        }

        if (request.getBusinessHours() != null) {
            store.updateBusinessHours(createBusinessHours(request, store));
        }

        return Response.of(StoreResponse.from(store));
    }


    @Transactional
    public void deleteStore(Long storeId, Long ownerId) {
        Store store = getStoreWithPermission(ownerId, storeId);
        store.setDeleted(true);
    }

    private Store getValidStore(Long storeId) {
        return storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));
    }

    private Store getStoreWithPermission(Long userId, Long storeId) {
        Store store = getValidStore(storeId);
        if (!Objects.equals(store.getOwner().getId(), userId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }
        return store;
    }

    private User getValidOwner(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        if (!owner.getUserType().equals(UserType.OWNER)) {
            throw new ClientException(ErrorCode.FORBIDDEN_STORE_CREATION);
        }
        return owner;
    }

    private List<StoreBusinessHour> createBusinessHours(StoreRequest request, Store store) {
        return request.getBusinessHours().stream()
                .map(req -> StoreBusinessHour.builder()
                        .store(store)
                        .dayOfWeek(req.getDayOfWeek())
                        .openTime(req.getOpenTime())
                        .closeTime(req.getCloseTime())
                        .isOpen(req.getIsOpen())
                        .build())
                .collect(Collectors.toList());
    }


    private List<StoreBusinessHour> createBusinessHours(List<StoreBusinessHourRequest> businessHourRequests, Store store) {
        return businessHourRequests.stream()
                .map(req -> StoreBusinessHour.builder()
                        .store(store)
                        .dayOfWeek(req.getDayOfWeek())
                        .openTime(req.getOpenTime())
                        .closeTime(req.getCloseTime())
                        .isOpen(req.getIsOpen())
                        .build())
                .collect(Collectors.toList());
    }
}
