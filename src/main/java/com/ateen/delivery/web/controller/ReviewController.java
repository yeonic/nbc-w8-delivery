package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.review.dto.request.ReviewSaveRequest;
import com.ateen.delivery.domain.review.dto.request.ReviewUpdateRequest;
import com.ateen.delivery.domain.review.dto.response.ReviewResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewSaveResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewUpdateResponse;
import com.ateen.delivery.domain.review.service.ReviewService;
import com.ateen.delivery.global.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/{storeId}")
public class ReviewController {

    private final ReviewService reviewService;

    //Store의 특정 Order에 대한 Review 생성
    @PostMapping("/orders/{orderId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<ReviewSaveResponse> save(
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @RequestBody @Valid ReviewSaveRequest request
    ) {
        return Response.of(reviewService.save(storeId, orderId, request));
    }

    //페이지네이션?? Response.of(T data, PagingResult page) 사용???
    //Store에 달린 Review 전체 조회.
    @GetMapping("/reviews")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<ReviewResponse>> findAll(
            @PathVariable Long storeId,
            @RequestParam(required = false) Integer stars
    ) {
        return Response.of(reviewService.findAll(storeId, stars));
    }

    //Store의 특정 Order에 대한 특정 Review 수정
    @PutMapping("/orders/{orderId}/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<ReviewUpdateResponse> update(
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }

        return Response.of(reviewService.update(userId, storeId, orderId, reviewId, request));
    }

    //Store의 특정 Order에 대한 특정 Review 삭제
    @DeleteMapping("/orders/{orderId}/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @PathVariable Long reviewId,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }

        reviewService.delete(userId, storeId, orderId, reviewId);
    }

}
