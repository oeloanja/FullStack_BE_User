package com.billit.user_service.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class ServiceAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String serviceName = request.getHeader("x-service-name");

        log.info("All Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("'{}': {}", headerName, request.getHeader(headerName));
        }

        log.info("Request URI: {}", request.getRequestURI());
        log.info("Service Name Header: {}", serviceName);

        if ("loan-group-service".equals(serviceName)||
                "investment-service".equals(serviceName)||
                "repayment-service".equals(serviceName)||
                "credit-proxy-service".equals(serviceName)) {
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(null, null, null));
        }

        filterChain.doFilter(request, response);
    }
}
