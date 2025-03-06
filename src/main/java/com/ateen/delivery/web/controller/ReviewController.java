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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/{storeId}")
public class ReviewController {

    private final ReviewService reviewService;

    //Store의 특정 Order에 대한 Review 생성
    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<Response<ReviewSaveResponse>> save(
            @PathVariable Long storeId,
            @PathVariable String orderId,
            @RequestBody @Valid ReviewSaveRequest request
    ) {
        ReviewSaveResponse response = reviewService.save(storeId, orderId, request);
        return ResponseEntity.created(URI.create("/reviews/" + response.getId())).build();
    }

    //페이지네이션?? Response.of(T data, PagingResult page) 사용???
    //Store에 달린 Review 전체 조회.
    @GetMapping("/reviews")
    public ResponseEntity<Response<List<ReviewResponse>>> findAll(
            @PathVariable Long storeId,
            @RequestParam(required = false) Integer stars
    ) {
        return ResponseEntity.ok(Response.of(reviewService.findAll(storeId, stars)));
    }

    //Store의 특정 Order에 대한 특정 Review 수정
    @PutMapping("/orders/{orderId}/reviews/{reviewId}")
    public ResponseEntity<Response<ReviewUpdateResponse>> update(
            @PathVariable Long storeId,
            @PathVariable String orderId,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }

        return ResponseEntity.ok(Response.of(reviewService.update(userId, storeId, orderId, reviewId, request)));
    }

    //Store의 특정 Order에 대한 특정 Review 삭제
    @DeleteMapping("/orders/{orderId}/reviews/{reviewId}")
    public void delete(
            @PathVariable Long storeId,
            @PathVariable String orderId,
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
