package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.orders.dto.request.OrderCreateRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderModiRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderStatusModiRequest;
import com.ateen.delivery.domain.orders.dto.response.OrderResponse;
import com.ateen.delivery.domain.orders.dto.response.OrderStatusResponse;
import com.ateen.delivery.domain.orders.service.OrderService;
import com.ateen.delivery.global.dto.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<OrderResponse> save(@RequestParam OrderCreateRequest request) {
        return Response.empty();
    }

    @GetMapping
    public Response<List<OrderResponse>> findOrders() {
        // TODO : 인가된 User를 넘겨주어, 관련된 Order만 가져오도록 변경

        return Response.emptyList();
    }

    @GetMapping("/{orderNum}")
    public Response<OrderResponse> findOrder(@PathVariable("orderNum") String orderNum) {
        // TODO : 인가된 User를 넘겨주어, 관련된 Order만 가져오도록 변경

        return Response.empty();
    }

    @PatchMapping("/{orderNum}")
    public Response<OrderResponse> updateOrder(
            @PathVariable("orderNum") String orderNum, @RequestBody OrderModiRequest request
    ) {
        // TODO : 인가된 User를 넘겨주어, 자신이 생성한 Order만 수정하도록 변경

        return Response.empty();
    }

    @GetMapping("/{orderNum}/status")
    public Response<OrderStatusResponse> findOrderStatus(@PathVariable("orderNum") String orderNum) {
        // TODO : 인가된 User를 넘겨주어, 관련된 Order만 가져오도록 변경

        return Response.empty();
    }

    @PatchMapping("/{orderNum}/status")
    public Response<OrderStatusResponse> updateOrderStatus(
            @PathVariable("orderNum") String orderNum, @RequestBody OrderStatusModiRequest request
    ) {
        // TODO : 인가된 User를 넘겨주어, 자신이 생성한 Order만 수정하도록 변경

        return Response.empty();
    }
}
