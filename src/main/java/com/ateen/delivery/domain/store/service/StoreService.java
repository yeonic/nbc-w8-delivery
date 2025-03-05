package com.ateen.delivery.domain.store.service;

import com.ateen.delivery.domain.common.exception.ForbiddenAccessException;
import com.ateen.delivery.domain.common.exception.UnauthorizedException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.store.dto.request.StoreUpdateRequest;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.store.dto.request.StoreCreateRequest;
import com.ateen.delivery.domain.store.dto.response.StoreResponse;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import com.ateen.delivery.global.dto.paging.PagingMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    /**
     * 가게 생성
     * 가게 수정
     * 가게 삭제 (soft delete)
     * 가게 단건 조회
    */

    @Transactional
    public ResponseEntity<Response<StoreResponse>> createStore(StoreCreateRequest request, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UnauthorizedException("사용자를 찾을 수 없습니다."));

        if (!owner.getUserType().equals(UserType.OWNER)) {
            throw new ForbiddenAccessException("사장님만 가게를 생성할 수 있습니다.");
        }

        long storeCount = storeRepository.countByOwnerId(ownerId);
        if (storeCount >= 3) {
            throw new ForbiddenAccessException("가게는 최대 3개까지 생성할 수 있습니다.");
        }

        Store store = Store.builder()
                .owner(owner)
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .address(new Address(
                        request.getCity(),
                        request.getDistrict(),
                        request.getStreet(),
                        request.getDetail()
                ))
                .openTime(request.getOpenTime())
                .closeTime(request.getCloseTime())
                .estimatedPickupTime(request.getEstimatedPickupTime())
                .minOrderAmount(request.getMinOrderAmount())
                .deliveryTip(request.getDeliveryTip())
                .isOpen(true)
                .isDeleted(false)
                .notice(request.getNotice())
                .build();


        storeRepository.save(store);
        return ResponseEntity.ok(Response.of(StoreResponse.from(store)));
    }

    @Transactional
    public ResponseEntity<Response<StoreResponse>> updateStore(Long storeId, StoreUpdateRequest request, Long ownerId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ForbiddenAccessException("존재하지 않거나 삭제된 가게입니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenAccessException("해당 가게를 수정할 권한이 없습니다.");
        }

        store.update(request);
        return ResponseEntity.ok(Response.of(StoreResponse.from(store)));
    }

    @Transactional
    public ResponseEntity<Void> deleteStore(Long storeId, Long ownerId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ForbiddenAccessException("존재하지 않거나 삭제된 가게입니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenAccessException("해당 가게를 삭제할 권한이 없습니다.");
        }

        store.setDeleted(true);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Response<List<StoreResponse>>> findAllStores(PagingCondition pagingCondition) {
        Page<Store> storePage = storeRepository.findAllByIsDeletedFalse(PagingCondition.toPageRequest(pagingCondition));

        List<StoreResponse> storeResponses = storePage.getContent().stream()
                .map(StoreResponse::from)
                .toList();

        return ResponseEntity.ok(Response.of(storeResponses, PagingMapper.toPagingRes(storePage, pagingCondition.getOrderBy().getValue())));
    }

    @Transactional
    public ResponseEntity<Response<StoreResponse>> findStoreById(Long storeId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ForbiddenAccessException("존재하지 않거나 삭제된 가게입니다."));

        return ResponseEntity.ok(Response.of(StoreResponse.from(store)));
    }
}