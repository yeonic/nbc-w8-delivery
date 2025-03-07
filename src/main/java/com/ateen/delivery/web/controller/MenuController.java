package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.auth.annotation.Authenticate;
import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.menu.dto.request.MenuSaveRequest;
import com.ateen.delivery.domain.menu.dto.request.MenuUpdateRequest;
import com.ateen.delivery.domain.menu.dto.response.MenuResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuSaveResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuUpdateResponse;
import com.ateen.delivery.domain.menu.service.MenuService;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.global.argresolver.annotation.LoginUser;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.error.ErrorCode;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/{storeId}/menus")
public class MenuController {

    private final MenuService menuService;
    private final StoreRepository storeRepository;

    @PostMapping
    @Authenticate
    public ResponseEntity<Response<MenuSaveResponse>> save(
            @PathVariable Long storeId,
            @RequestBody @Valid MenuSaveRequest request,
            @LoginUser AuthUser user
    ) {
        if (user == null) {
            throw new ClientException(ErrorCode.UNAUTHORIZED);
        }

        if (user.getUserType() != UserType.OWNER) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        Long storeOwnerId = storeRepository.findOwnerIdByStoreId(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        if (!user.getId().equals(storeOwnerId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        MenuSaveResponse response = menuService.save(user.getId(), storeId, request);
        return ResponseEntity.created(URI.create("/menus/" + response.getId())).build();
    }

    //페이지네이션?? Response.of(T data, PagingResult page) 사용???
    @GetMapping
    public ResponseEntity<Response<List<MenuResponse>>> findAll(@PathVariable Long storeId) {
        return ResponseEntity.ok(Response.of(menuService.findAll(storeId)));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<Response<MenuResponse>> findOne(
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ) {
        return ResponseEntity.ok(Response.of(menuService.findOne(storeId, menuId)));
    }

    @PutMapping("/{menuId}")
    @Authenticate
    public ResponseEntity<Response<MenuUpdateResponse>> update(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody @Valid MenuUpdateRequest request,
            @LoginUser AuthUser user
    ) {
        if (user == null) {
            throw new ClientException(ErrorCode.UNAUTHORIZED);
        }

        if (user.getUserType() != UserType.OWNER) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        Long storeOwnerId = storeRepository.findOwnerIdByStoreId(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        if (!user.getId().equals(storeOwnerId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        return ResponseEntity.ok(Response.of(menuService.update(storeId, menuId, request)));
    }

    @DeleteMapping("/{menuId}")
    @Authenticate
    public void delete(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @LoginUser AuthUser user
    ) {
        if (user == null) {
            throw new ClientException(ErrorCode.UNAUTHORIZED);
        }

        if (user.getUserType() != UserType.OWNER) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        Long storeOwnerId = storeRepository.findOwnerIdByStoreId(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        if (!user.getId().equals(storeOwnerId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        menuService.delete(storeId, menuId);
    }

}
