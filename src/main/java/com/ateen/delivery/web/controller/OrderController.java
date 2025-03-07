package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.auth.annotation.Authenticate;
import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.orders.dto.request.OrderCreateRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderModiRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderStatusModiRequest;
import com.ateen.delivery.domain.orders.dto.response.OrderResponse;
import com.ateen.delivery.domain.orders.dto.response.OrderStatusResponse;
import com.ateen.delivery.domain.orders.service.OrderReadService;
import com.ateen.delivery.domain.orders.service.OrderWriteService;
import com.ateen.delivery.global.argresolver.annotation.LoginUser;
import com.ateen.delivery.global.argresolver.annotation.PageCond;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import com.ateen.delivery.global.dto.paging.PagingResult;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderReadService readService;
    private final OrderWriteService writeService;

    @PostMapping
    @Authenticate
    public ResponseEntity<Response<OrderResponse>> save(
            @Valid @RequestBody OrderCreateRequest request, @LoginUser AuthUser currentUser
    ) {
        OrderResponse saved = writeService.save(request, LocalDateTime.now(), currentUser.getId());
        String createdUri = String.format("/api/orders/%s", saved.getOrderId());

        return ResponseEntity.created(URI.create(createdUri)).body(Response.of(saved));
    }

    @GetMapping
    @Authenticate
    public ResponseEntity<Response<List<OrderResponse>>> findOrders(
            @PageCond PagingCondition condition, @LoginUser AuthUser currentUser
    ) {
        Page<OrderResponse> page = readService.findAll(condition, currentUser.getId());
        return ResponseEntity.ok(Response.of(page.getContent(), PagingResult.of(page, condition)));
    }

    @GetMapping("/{orderNum}")
    @Authenticate
    public ResponseEntity<Response<OrderResponse>> findOrder(
            @PathVariable("orderNum") String orderNum, @LoginUser AuthUser currentUser
    ) {
        return ResponseEntity.ok(
                Response.of(readService.findOrder(orderNum, currentUser.getId()))
        );
    }


    @PatchMapping("/{orderNum}")
    @Authenticate
    public ResponseEntity<Response<OrderResponse>> updateOrder(
            @PathVariable("orderNum") String orderNum, @Valid @RequestBody OrderModiRequest request,
            @LoginUser AuthUser user
    ) {
        return ResponseEntity.ok(Response.of(writeService.updateOrder(orderNum, request, user.getId())));
    }


    @Authenticate
    @PostMapping("/{orderNum}/cancel")
    public ResponseEntity<Response<OrderResponse>> cancelOrder(
            @PathVariable("orderNum") String orderNum, @LoginUser AuthUser authUser
    ) {
        return ResponseEntity.ok(Response.of(writeService.cancelOrder(orderNum, authUser.getId())));
    }

    @GetMapping("/{orderNum}/status")
    @Authenticate
    public ResponseEntity<Response<OrderStatusResponse>> findOrderStatus(
            @PathVariable("orderNum") String orderNum, @LoginUser AuthUser currentUser
    ) {
        return ResponseEntity.ok(Response.of(readService.findOrderStatus(orderNum, currentUser.getId())));
    }

    @PatchMapping("/{orderNum}/status")
    @Authenticate
    public ResponseEntity<Response<OrderStatusResponse>> updateOrderStatus(
            @PathVariable("orderNum") String orderNum, @Valid @RequestBody OrderStatusModiRequest request,
            @LoginUser AuthUser user
    ) {
        return ResponseEntity.ok(
                Response.of(writeService.updateOrderStatus(orderNum, request, LocalDateTime.now(), user)));
    }

    @DeleteMapping("/{orderNum}")
    @Authenticate
    public ResponseEntity<Void> deleteOrderStatus(
            @PathVariable("orderNum") String orderNum, @LoginUser AuthUser user
    ) {
        writeService.delete(orderNum, user.getId());
        return ResponseEntity.noContent().build();
    }
}
