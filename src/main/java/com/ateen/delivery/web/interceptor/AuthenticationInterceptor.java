package com.ateen.delivery.web.interceptor;

import com.ateen.delivery.domain.auth.JwtUtil;
import com.ateen.delivery.domain.auth.annotation.Authenticate;
import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.global.constants.KeyConst;
import com.ateen.delivery.global.dto.error.ErrorCode;
import io.jsonwebtoken.Claims;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (isNotApplicable(handler)) {
            return true;
        }

        String header = request.getHeader(KeyConst.AUTHORIZATION_HEADER);

        if (header == null || !jwtUtil.verifyToken(header)) {
            throw new ClientException(ErrorCode.INVALID_TOKEN);
        }

        Claims jwtPayload = jwtUtil.getJwtPayload(header);
        long userId = Long.parseLong(jwtPayload.getSubject());
        String email = (String) jwtPayload.get(KeyConst.JWT_CLAIM_EMAIL);
        UserType userType = UserType.valueOf((String) jwtPayload.get(KeyConst.JWT_CLAIM_USER_TYPE));

        request.setAttribute(KeyConst.AUTH_USER, new AuthUser(userId, email, userType));
        return true;
    }

    private boolean isNotApplicable(Object handler) {
        return !(handler instanceof HandlerMethod)
                || ((HandlerMethod) handler).getMethodAnnotation(Authenticate.class) == null;
    }
}
