package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.menu.dto.request.MenuSaveRequest;
import com.ateen.delivery.domain.menu.dto.request.MenuUpdateRequest;
import com.ateen.delivery.domain.menu.dto.response.MenuResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuSaveResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuUpdateResponse;
import com.ateen.delivery.domain.menu.service.MenuService;
import com.ateen.delivery.global.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/{storeId}")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/menus")
    public Response<MenuSaveResponse> save(
            @PathVariable Long storeId,
            @RequestBody MenuSaveRequest request,
            HttpServletRequest httpRequest
    ) {
        Long ownerId = (Long) httpRequest.getAttribute("ownerId");
        if (ownerId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }

        return Response.of(menuService.save(ownerId, storeId, request));
    }

    //페이지네이션?? Response.of(T data, PagingResult page) 사용???
    @GetMapping("/menus")
    public Response<List<MenuResponse>> findAll(@PathVariable Long storeId) {
        return Response.of(menuService.findAll(storeId));
    }

    @GetMapping("/menus/{menuId}")
    public Response<MenuResponse> findOne(
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ) {
        return Response.of(menuService.findOne(storeId, menuId));
    }

    @PutMapping("/menus/{menuId}")
    public Response<MenuUpdateResponse> update(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody MenuUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long ownerId = (Long) httpRequest.getAttribute("ownerId");
        if (ownerId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }

        return Response.of(menuService.update(ownerId, storeId, menuId, request));
    }

    @DeleteMapping("/menus/{menuId}")
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
