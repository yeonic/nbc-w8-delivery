package com.ateen.delivery.global.argresolver;

import com.ateen.delivery.global.argresolver.annotation.PageCond;
import com.ateen.delivery.global.dto.paging.PagingCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class PageCondArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(PageCond.class);
        boolean assignableFrom = PagingCondition.class.isAssignableFrom(parameter.getParameterType());

        return hasParameterAnnotation && assignableFrom;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String pageNum = webRequest.getParameter("pageNum");
        String pageSize = webRequest.getParameter("pageSize");
        String orderBy = webRequest.getParameter("orderBy");
        String orderDirection = webRequest.getParameter("orderDirection");

        return PagingCondition.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderDirection(orderDirection)
                .build();
    }
}
