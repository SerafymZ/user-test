package com.usertest.filter;


import org.apache.catalina.connector.RequestFacade;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
public class CustomFilter implements Filter {

    private static final String SAVE_USER_URL = "http://localhost:8080/user";
    private int saveUserRequestCounter = 0;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request != null && request instanceof RequestFacade) {
            var requestFacade = ((RequestFacade) request);
            if (HttpMethod.POST.name().equals(requestFacade.getMethod())
                    && requestFacade.getRequestURL() != null
                    && SAVE_USER_URL.equals(requestFacade.getRequestURL().toString())){
                System.out.println("Received a request to save a user. Request number: " + ++saveUserRequestCounter);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
