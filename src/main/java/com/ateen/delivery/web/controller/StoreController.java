package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.store.dto.response.StoreResponse;
import com.ateen.delivery.domain.store.service.StoreService;
import com.ateen.delivery.domain.store.dto.request.StoreCreateRequest;
import com.ateen.delivery.domain.store.dto.request.StoreUpdateRequest;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import com.ateen.delivery.global.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<Response<StoreResponse>> createStore(
            @RequestBody StoreCreateRequest request,
            @RequestParam(value = "ownerId", required = false) Long ownerId
    ) {
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId 값이 필요합니다.");
        }
        return storeService.createStore(request, ownerId);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<Response<StoreResponse>> updateStore(
            @PathVariable("storeId") Long storeId,  // @PathVariable 명시적으로 지정
            @RequestBody StoreUpdateRequest request,
            @RequestParam(name = "ownerId") Long ownerId  // @RequestParam 명확하게 지정
    ) {
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId 값이 필요합니다.");
        }
        return storeService.updateStore(storeId, request, ownerId);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable("storeId") Long storeId,
            @RequestParam(name = "ownerId") Long ownerId
    ) {
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId 값이 필요합니다.");
        }
        return storeService.deleteStore(storeId, ownerId);
    }


    @GetMapping
    public ResponseEntity<Response<List<StoreResponse>>> getAllStores(
            @ModelAttribute PagingCondition pagingCondition
    ) {
        return storeService.findAllStores(pagingCondition);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<Response<StoreResponse>> getStoreDetail(@PathVariable("storeId") Long storeId) {
        return storeService.findStoreById(storeId);
    }
}
