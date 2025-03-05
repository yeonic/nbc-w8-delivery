package com.ateen.delivery.web.interceptor;

import com.ateen.delivery.global.constants.KeyConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String bearerToken = request.getHeader(KeyConst.AUTHORIZATION_HEADER);

        return true;
    }
}
