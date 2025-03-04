package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.review.dto.request.ReviewSaveRequest;
import com.ateen.delivery.domain.review.dto.request.ReviewUpdateRequest;
import com.ateen.delivery.domain.review.dto.response.ReviewResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewSaveResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewUpdateResponse;
import com.ateen.delivery.domain.review.service.ReviewService;
import com.ateen.delivery.global.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders/{orderNum}")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<ReviewSaveResponse> save(
            @PathVariable Long orderNum,
            @RequestBody ReviewSaveRequest request
    ) {
        return Response.of(reviewService.save(orderNum, request));
    }

    //페이지네이션?? Response.of(T data, PagingResult page) 사용???
    @GetMapping("/reviews")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<ReviewResponse>> findAll(@PathVariable Long orderNum) {
        return Response.of(reviewService.findAll(orderNum));
    }

    @GetMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<ReviewResponse> findOne(
            @PathVariable Long orderNum,
            @PathVariable Long reviewId
    ) {
        return Response.of(reviewService.findOne(orderNum, reviewId));
    }

    @PutMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<ReviewUpdateResponse> update(
            @PathVariable Long orderNum,
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }

        return Response.of(reviewService.update(userId, orderNum, reviewId, request));
    }

    @DeleteMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable Long orderNum,
            @PathVariable Long reviewId,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저가 인증되지 않았습니다.");
        }

        reviewService.delete(userId, orderNum, reviewId);
    }

}
