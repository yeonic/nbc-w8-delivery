package com.ateen.delivery.domain.store.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.store.dto.request.StoreBusinessHourRequest;
import com.ateen.delivery.domain.store.dto.request.StoreRequest;
import com.ateen.delivery.domain.store.dto.response.StoreResponse;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.error.ErrorCode;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import com.ateen.delivery.global.dto.paging.PagingMapper;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    private static final Set<DayOfWeek> REQUIRED_DAYS = EnumSet.allOf(DayOfWeek.class);

    @Transactional
    public Response<StoreResponse> createStore(StoreRequest request, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        if (!owner.getUserType().equals(UserType.OWNER)) {
            throw new ClientException(ErrorCode.FORBIDDEN_STORE_CREATION);
        }

        long storeCount = storeRepository.countByOwnerId(ownerId);
        if (storeCount >= 3) {
            throw new ClientException(ErrorCode.STORE_OVER_CREATION);
        }

        Address address = new Address(
                request.getCity(),
                request.getDistrict(),
                request.getStreet(),
                request.getDetail()
        );

        List<StoreBusinessHour> businessHours = request.getBusinessHours().stream()
                .map(req -> StoreBusinessHour.builder()
                        .dayOfWeek(req.getDayOfWeek())
                        .openTime(req.getOpenTime())
                        .closeTime(req.getCloseTime())
                        .isOpen(req.getIsOpen())
                        .build())
                .collect(Collectors.toList());

        Store store = Store.createStore(
                owner,
                request.getName(),
                request.getPhoneNumber(),
                address,
                request.getNotice(),
                request.getEstimatedPickupTime(),
                request.getMinOrderAmount(),
                request.getDeliveryTip(),
                request.isOpen(),
                businessHours
        );

        storeRepository.save(store);
        return Response.of(StoreResponse.from(store));
    }

    @Transactional
    public Response<List<StoreResponse>> findAllStores(PagingCondition pagingCondition) {
        Page<Store> storePage = storeRepository.findAllByIsDeletedFalse(PagingCondition.toPageRequest(pagingCondition));

        List<StoreResponse> storeResponses = storePage.getContent().stream()
                .map(StoreResponse::from)
                .toList();

        return Response.of(storeResponses,
                PagingMapper.toPagingRes(storePage, pagingCondition.getOrderBy().getValue()));
    }

    @Transactional
    public Response<StoreResponse> findStoreById(Long storeId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        return Response.of(StoreResponse.from(store));
    }

    @Transactional
    public Response<StoreResponse> updateStore(Long storeId, StoreRequest request, Long ownerId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        Address updatedAddress = new Address(
                request.getCity(),
                request.getDistrict(),
                request.getStreet(),
                request.getDetail()
        );

        store.update(request.getName(), request.getPhoneNumber(), updatedAddress, request.getNotice());

        return Response.of(StoreResponse.from(store));
    }

    @Transactional
    public void deleteStore(Long storeId, Long ownerId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        store.setDeleted(true);
    }

    @Transactional
    public Response<StoreResponse> updateBusinessHours(Long storeId,
            List<StoreBusinessHourRequest> businessHourRequests, Long ownerId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        List<StoreBusinessHour> newHours = businessHourRequests.stream()
                .map(req -> StoreBusinessHour.builder()
                        .store(store)
                        .dayOfWeek(req.getDayOfWeek())
                        .openTime(req.getOpenTime())
                        .closeTime(req.getCloseTime())
                        .isOpen(req.getIsOpen())
                        .build())
                .collect(Collectors.toList());

        store.updateBusinessHours(newHours);

        return Response.of(StoreResponse.from(store));
    }

    @Transactional
    public Response<List<StoreResponse>> searchStoresByName(String name, PagingCondition pagingCondition) {
        Pageable pageable = PagingCondition.toPageRequest(pagingCondition);

        Page<Store> storePage;
        if (name == null || name.trim().isEmpty()) {
            storePage = storeRepository.findAllByIsDeletedFalse(pageable); // 전체 조회
        } else {
            storePage = storeRepository.findByNameContainingAndIsDeletedFalse(name, pageable);
        }

        List<StoreResponse> storeResponses = storePage.getContent().stream()
                .map(StoreResponse::from)
                .toList();

        return Response.of(storeResponses,
                PagingMapper.toPagingRes(storePage, pagingCondition.getOrderBy().getValue()));
    }

}
