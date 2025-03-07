package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.store.dto.request.StoreHolidayRequest;
import com.ateen.delivery.domain.store.dto.response.StoreHolidayResponse;
import com.ateen.delivery.domain.store.service.StoreHolidayService;
import com.ateen.delivery.domain.auth.annotation.Authenticate;
import com.ateen.delivery.global.argresolver.annotation.LoginUser;
import com.ateen.delivery.global.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores/{storeId}/holidays")
@RequiredArgsConstructor
public class StoreHolidayController {

    private final StoreHolidayService storeHolidayService;

    @Authenticate
    @PostMapping
    public ResponseEntity<Response<StoreHolidayResponse>> addHoliday(
            @PathVariable Long storeId,
            @LoginUser AuthUser authUser,
            @Valid @RequestBody StoreHolidayRequest request) {

        Response<StoreHolidayResponse> response = storeHolidayService.addHoliday(authUser.getId(), storeId, request);
        return ResponseEntity.ok(response);
    }

    @Authenticate
    @DeleteMapping("/{holidayId}")
    public ResponseEntity<Void> removeHoliday(
            @PathVariable Long storeId,
            @PathVariable Long holidayId,
            @LoginUser AuthUser authUser) {

        storeHolidayService.removeHoliday(authUser.getId(), storeId, holidayId);
        return ResponseEntity.noContent().build();
    }
}
