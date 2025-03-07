package com.ateen.delivery.aspect;

import com.ateen.delivery.domain.orders.dto.response.OrderInfo;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(public * com.ateen.delivery.domain.orders.service.OrderWriteService.*(.., java.time.LocalDateTime))")
    public Object logAfterManipulateOrder(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        LocalDateTime requestedTime = (LocalDateTime) args[args.length - 1];

        OrderInfo result = (OrderInfo) joinPoint.proceed();

        // TODO : 가게 id 포함
        log.info("[{}] [MOD ORDER] [{} {}] [orderId: {}] [storeName: {}]",
                requestedTime, request.getMethod(), request.getRequestURI(), result.getOrderId(),
                result.getStoreName());

        return result;
    }
}
