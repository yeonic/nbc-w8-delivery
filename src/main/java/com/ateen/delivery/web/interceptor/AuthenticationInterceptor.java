package com.ateen.delivery.web.interceptor;

import com.ateen.delivery.domain.auth.JwtUtil;
import com.ateen.delivery.domain.auth.annotation.Authenticate;
import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.domain.user.repository.UserRepository;
import com.ateen.delivery.global.constants.KeyConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (isNotApplicable(handler)) {
            return true;
        }

        String header = request.getHeader(KeyConst.AUTHORIZATION_HEADER);

        if (header == null || !jwtUtil.verifyToken(header)) {
            throw new IllegalStateException("토큰이 유효하지 않습니다.");
        }

        String userEmail = jwtUtil.extractEmail(header);

        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException("유저가 존재하지 않습니다.")
        );

        request.setAttribute(KeyConst.AUTH_USER, AuthUser.fromUser(user));
        return true;
    }

    private boolean isNotApplicable(Object handler) {
        return !(handler instanceof HandlerMethod)
                || ((HandlerMethod) handler).getMethodAnnotation(Authenticate.class) == null;
    }
}
