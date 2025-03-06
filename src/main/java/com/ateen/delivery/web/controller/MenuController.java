package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.menu.dto.request.MenuSaveRequest;
import com.ateen.delivery.domain.menu.dto.request.MenuUpdateRequest;
import com.ateen.delivery.domain.menu.dto.response.MenuResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuSaveResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuUpdateResponse;
import com.ateen.delivery.domain.menu.service.MenuService;
import com.ateen.delivery.global.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/{storeId}/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Response<MenuSaveResponse>> save(
            @PathVariable Long storeId,
            @RequestBody @Valid MenuSaveRequest request,
            HttpServletRequest httpRequest
    ) {
        Long ownerId = (Long) httpRequest.getAttribute("ownerId");
        if (ownerId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }
        MenuSaveResponse response = menuService.save(ownerId, storeId, request);
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
    public ResponseEntity<Response<MenuUpdateResponse>> update(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody @Valid MenuUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long ownerId = (Long) httpRequest.getAttribute("ownerId");
        if (ownerId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }

        return ResponseEntity.ok(Response.of(menuService.update(ownerId, storeId, menuId, request)));
    }

    @DeleteMapping("/{menuId}")
    public void delete(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            HttpServletRequest httpRequest
    ) {
        Long ownerId = (Long) httpRequest.getAttribute("ownerId");
        if (ownerId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }

        menuService.delete(ownerId, storeId, menuId);
    }

}
