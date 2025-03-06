package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.store.dto.request.StoreRequest;
import com.ateen.delivery.domain.store.dto.request.StoreBusinessHourRequest;
import com.ateen.delivery.domain.store.dto.response.StoreResponse;
import com.ateen.delivery.domain.store.service.StoreService;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<Response<StoreResponse>> createStore(
            @RequestBody StoreRequest request,
            @RequestParam("ownerId") Long ownerId
    ) {
        Response<StoreResponse> response = storeService.createStore(request, ownerId);

        URI location = URI.create("/api/stores/" + response.getData().getId());

        return ResponseEntity.created(location).body(response);
    }
    @PutMapping("/{storeId}")
    public ResponseEntity<Response<StoreResponse>> updateStore(
            @PathVariable("storeId") Long storeId,
            @RequestBody StoreRequest request,
            @RequestParam("ownerId") Long ownerId
    ) {
        return ResponseEntity.ok(storeService.updateStore(storeId, request, ownerId));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable("storeId") Long storeId,
            @RequestParam("ownerId") Long ownerId
    ) {
        storeService.deleteStore(storeId, ownerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Response<List<StoreResponse>>> getAllStores(@ModelAttribute PagingCondition pagingCondition) {
        return ResponseEntity.ok(storeService.findAllStores(pagingCondition));
    }


    @GetMapping("/{storeId}")
    public ResponseEntity<Response<StoreResponse>> getStoreDetail(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(storeService.findStoreById(storeId));
    }

    @PutMapping("/{storeId}/business-hours")
    public ResponseEntity<Response<StoreResponse>> updateBusinessHours(
            @PathVariable("storeId") Long storeId,
            @RequestBody List<StoreBusinessHourRequest> businessHourRequests,
            @RequestParam("ownerId") Long ownerId
    ) {
        return ResponseEntity.ok(storeService.updateBusinessHours(storeId, businessHourRequests, ownerId));
    }

    @GetMapping("/search")
    public ResponseEntity<Response<List<StoreResponse>>> searchStores(
            @RequestParam(value = "name", required = false) String name,
            @Valid PagingCondition pagingCondition) {

        Response<List<StoreResponse>> response = storeService.searchStoresByName(name, pagingCondition);
        return ResponseEntity.ok(response);
    }

}
