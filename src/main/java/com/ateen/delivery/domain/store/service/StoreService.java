package com.ateen.delivery.domain.store.service;

import com.ateen.delivery.domain.common.exception.ForbiddenAccessException;
import com.ateen.delivery.domain.common.exception.UnauthorizedException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.store.dto.request.StoreBusinessHourRequest;
import com.ateen.delivery.domain.store.dto.request.StoreRequest;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.store.dto.response.StoreResponse;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.argresolver.annotation.PageCond;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import com.ateen.delivery.global.dto.paging.PagingMapper;
import com.ateen.delivery.global.dto.paging.PagingResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    private static final Set<DayOfWeek> REQUIRED_DAYS = EnumSet.allOf(DayOfWeek.class);

    @Transactional
    public Response<StoreResponse> createStore(StoreRequest request, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UnauthorizedException("사용자를 찾을 수 없습니다."));

        if (!owner.getUserType().equals(UserType.OWNER)) {
            throw new ForbiddenAccessException("사장님만 가게를 생성할 수 있습니다.");
        }

        long storeCount = storeRepository.countByOwnerId(ownerId);
        if (storeCount >= 3) {
            throw new ForbiddenAccessException("가게는 최대 3개까지 생성할 수 있습니다.");
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
    public Page<StoreResponse> findAllStores(PagingCondition pagingCondition) {
        Pageable pageable = PagingCondition.toPageRequest(pagingCondition);
        return storeRepository.findAllByIsDeletedFalse(pageable)
                .map(StoreResponse::from);
    }

    @Transactional
    public Response<StoreResponse> findStoreById(Long storeId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ForbiddenAccessException("존재하지 않거나 삭제된 가게입니다."));

        return Response.of(StoreResponse.from(store));
    }

    @Transactional
    public Response<StoreResponse> updateStore(Long storeId, StoreRequest request, Long ownerId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ForbiddenAccessException("존재하지 않거나 삭제된 가게입니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenAccessException("해당 가게를 수정할 권한이 없습니다.");
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
                .orElseThrow(() -> new ForbiddenAccessException("존재하지 않거나 삭제된 가게입니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenAccessException("해당 가게를 삭제할 권한이 없습니다.");
        }

        store.setDeleted(true);
    }

    @Transactional
    public Response<StoreResponse> updateBusinessHours(Long storeId, List<StoreBusinessHourRequest> businessHourRequests, Long ownerId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ForbiddenAccessException("존재하지 않거나 삭제된 가게입니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenAccessException("해당 가게의 영업 시간을 수정할 권한이 없습니다.");
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
    public Page<StoreResponse> searchStoresByName(String name, PagingCondition pagingCondition) {
        Pageable pageable = PagingCondition.toPageRequest(pagingCondition);

        return (name == null || name.trim().isEmpty()) ?
                storeRepository.findAllByIsDeletedFalse(pageable).map(StoreResponse::from) :
                storeRepository.findByNameContainingAndIsDeletedFalse(name, pageable).map(StoreResponse::from);
    }
}
