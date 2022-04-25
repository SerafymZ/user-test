package com.usertest.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    private AtomicInteger saveUserRequestCounter = new AtomicInteger(0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request != null && HttpMethod.POST.name().equals(request.getMethod())) {
            log.info("Received a request to save a user. Request number: " + saveUserRequestCounter.incrementAndGet());
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}

