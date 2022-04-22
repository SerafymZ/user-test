package com.usertest.logging;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingDispatcherServlet extends DispatcherServlet {

    private int saveUserRequestCounter = 0;

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(request != null && HttpMethod.POST.name().equals(request.getMethod()) &&
                request.getRequestURL() != null && request.getRequestURL().toString().contains("user")){
            synchronized (this) {
                System.out.println("Received a request to save a user. Request number: " + ++saveUserRequestCounter);
            }
        }

        super.doDispatch(request, response);
    }
}
