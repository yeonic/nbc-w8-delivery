package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.store.dto.request.StoreRequest;
import com.ateen.delivery.domain.store.dto.request.StoreBusinessHourRequest;
import com.ateen.delivery.domain.store.dto.response.StoreResponse;
import com.ateen.delivery.domain.store.service.StoreService;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import com.ateen.delivery.global.dto.paging.PagingResult;
import com.ateen.delivery.domain.auth.annotation.Authenticate;
import com.ateen.delivery.global.argresolver.annotation.LoginUser;
import com.ateen.delivery.global.argresolver.annotation.PageCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @Authenticate
    @PostMapping
    public ResponseEntity<Response<StoreResponse>> createStore(
            @RequestBody StoreRequest request,
            @LoginUser AuthUser authUser
    ) {
        Response<StoreResponse> response = storeService.createStore(request, authUser.getId());
        return ResponseEntity.created(URI.create("/api/stores/" + response.getData().getId())).body(response);
    }

    @Authenticate
    @PutMapping("/{storeId}")
    public ResponseEntity<Response<StoreResponse>> updateStore(
            @PathVariable Long storeId,
            @RequestBody StoreRequest request,
            @LoginUser AuthUser authUser
    ) {
        return ResponseEntity.ok(storeService.updateStore(storeId, request, authUser.getId()));
    }

    @Authenticate
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable Long storeId,
            @LoginUser AuthUser authUser
    ) {
        storeService.deleteStore(storeId, authUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Response<List<StoreResponse>>> searchStores(
            @RequestParam(value = "name", required = false) String name,
            @PageCond PagingCondition pagingCondition) {

        Page<StoreResponse> page = storeService.searchStores(name, pagingCondition);
        return ResponseEntity.ok(Response.of(page.getContent(), PagingResult.of(page, pagingCondition)));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<Response<StoreResponse>> getStoreDetail(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.findStoreById(storeId));
    }
}
