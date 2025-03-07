package com.ateen.delivery.web.controller;

import com.ateen.delivery.domain.auth.annotation.Authenticate;
import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.review.dto.request.ReviewSaveRequest;
import com.ateen.delivery.domain.review.dto.request.ReviewUpdateRequest;
import com.ateen.delivery.domain.review.dto.response.ReviewResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewSaveResponse;
import com.ateen.delivery.domain.review.dto.response.ReviewUpdateResponse;
import com.ateen.delivery.domain.review.service.ReviewService;
import com.ateen.delivery.global.argresolver.annotation.LoginUser;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/{storeId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //Store에 대한 Review 생성
    @PostMapping
    @Authenticate
    public ResponseEntity<Response<ReviewSaveResponse>> save(
            @PathVariable Long storeId,
            @RequestBody ReviewSaveRequest request,
            @LoginUser AuthUser user
    ) {
        ReviewSaveResponse response = reviewService.save(storeId, user.getId(), request);
        return ResponseEntity.created(URI.create("/reviews/" + response.getId())).build();
    }

    //페이지네이션?? Response.of(T data, PagingResult page) 사용???
    //Store에 달린 Review 전체 조회.
    @GetMapping
    public ResponseEntity<Response<List<ReviewResponse>>> findAll(
            @PathVariable Long storeId,
            @RequestParam(required = false) Integer stars   //시작지점과 끝지점이 있어야 한다. 두개의 값이 있을 경우 범위 지정.
    ) {
        return ResponseEntity.ok(Response.of(reviewService.findAll(storeId, stars)));
    }

    //Store에 대한 특정 Review 수정
    @PutMapping("/{reviewId}")
    @Authenticate
    public ResponseEntity<Response<ReviewUpdateResponse>> update(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request,
            @LoginUser AuthUser user
    ) {
        return ResponseEntity.ok(Response.of(reviewService.update(user.getId(), storeId, reviewId, request)));
    }

    //Store에 대한 특정 Review 삭제
    @DeleteMapping("/{reviewId}")
    @Authenticate
    public void delete(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @LoginUser AuthUser user
    ) {
        reviewService.delete(user.getId(), storeId, reviewId);
    }

}
