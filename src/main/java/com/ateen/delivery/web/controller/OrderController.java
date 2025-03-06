package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.orders.dto.request.OrderCreateRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderModiRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderStatusModiRequest;
import com.ateen.delivery.domain.orders.dto.response.OrderResponse;
import com.ateen.delivery.domain.orders.dto.response.OrderStatusResponse;
import com.ateen.delivery.domain.orders.service.OrderReadService;
import com.ateen.delivery.domain.orders.service.OrderWriteService;
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
    public ResponseEntity<Response<OrderResponse>> save(@Valid @RequestBody OrderCreateRequest request) {
        OrderResponse saved = writeService.save(request, LocalDateTime.now());
        String createdUri = String.format("/api/orders/%s", saved.getOrderId());

        return ResponseEntity.created(URI.create(createdUri)).body(Response.of(saved));
    }

    @GetMapping
    public ResponseEntity<Response<List<OrderResponse>>> findOrders(@PageCond PagingCondition condition) {
        // TODO : 인가된 User를 넘겨주어, 관련된 Order만 가져오도록 변경

        Page<OrderResponse> page = readService.findAll(condition);
        return ResponseEntity.ok(Response.of(page.getContent(), PagingResult.of(page, condition)));
    }

    @GetMapping("/{orderNum}")
    public ResponseEntity<Response<OrderResponse>> findOrder(@PathVariable("orderNum") String orderNum) {
        // TODO : 인가된 User를 넘겨주어, 관련된 Order만 가져오도록 변경

        return ResponseEntity.ok(Response.of(readService.findOrder(orderNum)));
    }


    @PatchMapping("/{orderNum}")
    public ResponseEntity<Response<OrderResponse>> updateOrder(
            @PathVariable("orderNum") String orderNum, @Valid @RequestBody OrderModiRequest request
    ) {
        // TODO : 인가된 User를 넘겨주어, 자신이 생성한 Order만 수정하도록 변경

        return ResponseEntity.ok(Response.of(writeService.updateOrder(orderNum, request)));
    }


    @PostMapping("/{orderNum}/cancel")
    public ResponseEntity<Response<OrderResponse>> cancelOrder(@PathVariable("orderNum") String orderNum) {
        // TODO : 인가된 User를 넘겨주어, 자신이 생성한 Order만 취소/거절하도록 변경

        return ResponseEntity.ok(Response.of(writeService.cancelOrder(orderNum)));
    }

    @GetMapping("/{orderNum}/status")
    public ResponseEntity<Response<OrderStatusResponse>> findOrderStatus(@PathVariable("orderNum") String orderNum) {
        // TODO : 인가된 User를 넘겨주어, 관련된 Order만 가져오도록 변경

        return ResponseEntity.ok(Response.of(readService.getOrderStatus(orderNum)));
    }

    @PatchMapping("/{orderNum}/status")
    public ResponseEntity<Response<OrderStatusResponse>> updateOrderStatus(
            @PathVariable("orderNum") String orderNum, @Valid @RequestBody OrderStatusModiRequest request
    ) {
        // TODO : 인가된 User를 넘겨주어, 자신이 생성한 Order만 수정하도록 변경

        return ResponseEntity.ok(Response.of(writeService.updateOrderStatus(orderNum, request, LocalDateTime.now())));
    }

    @DeleteMapping("/{orderNum}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable("orderNum") String orderNum) {
        writeService.delete(orderNum);
        return ResponseEntity.noContent().build();
    }
}
