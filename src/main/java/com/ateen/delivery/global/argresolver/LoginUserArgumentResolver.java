package com.ateen.delivery.global.argresolver;

import com.ateen.delivery.domain.auth.dto.AuthUser;
import com.ateen.delivery.domain.common.exception.UnauthorizedException;
import com.ateen.delivery.global.argresolver.annotation.LoginUser;
import com.ateen.delivery.global.constants.KeyConst;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean assignableFrom = AuthUser.class.isAssignableFrom(parameter.getParameterType());

        return hasParameterAnnotation && assignableFrom;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        return Optional.ofNullable(nativeRequest.getAttribute(KeyConst.AUTH_USER))
                .orElseThrow(() -> new UnauthorizedException("로그인이 필요합니다."));
    }
}
