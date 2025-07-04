package com.shine.shine.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.shine.shine.Entity.SessionLog;
import com.shine.shine.Entity.Users;
import com.shine.shine.Service.SessionLogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final SessionLogService sessionLogService;

    public LoggingInterceptor(SessionLogService sessionLogService) {
        this.sessionLogService = sessionLogService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Store start time for duration calculation
        request.setAttribute("startTime", System.currentTimeMillis());
        
        // Generate session ID if not exists
        String sessionId = (String) request.getSession().getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            request.getSession().setAttribute("sessionId", sessionId);
        }
        
        // Store session ID in request attributes
        request.setAttribute("sessionId", sessionId);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            // Get start time
            Long startTime = (Long) request.getAttribute("startTime");
            String sessionId = (String) request.getAttribute("sessionId");
            
            if (startTime != null && sessionId != null) {
                // Calculate duration
                long durationMs = System.currentTimeMillis() - startTime;
                
                // Get current user
                Users user = getCurrentUser();
                
                // Get request details
                String ipAddress = getClientIpAddress(request);
                String userAgent = request.getHeader("User-Agent");
                String method = request.getMethod();
                String endpoint = request.getRequestURI();
                String action = determineAction(method, endpoint);
                Integer statusCode = response.getStatus();
                String requestBody = getRequestBody(request);
                String responseBody = ""; // We don't capture response body for performance
                String errorMessage = ex != null ? ex.getMessage() : null;
                
                // Create session log
                SessionLog sessionLog = new SessionLog(user, sessionId, ipAddress, userAgent, action, endpoint, method);
                sessionLog.setStatusCode(statusCode);
                sessionLog.setRequestBody(requestBody);
                sessionLog.setResponseBody(responseBody);
                sessionLog.setDurationMs(durationMs);
                sessionLog.setErrorMessage(errorMessage);
                sessionLog.setTimestamp(LocalDateTime.now());
                
                // Save the log
                sessionLogService.saveSessionLog(sessionLog);
            }
        } catch (Exception e) {
            // Don't let logging errors affect the main application
            System.err.println("Error logging session: " + e.getMessage());
        }
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Users) {
            return (Users) authentication.getPrincipal();
        }
        return null;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    private String determineAction(String method, String endpoint) {
        if (endpoint.startsWith("/api/auth")) {
            if (endpoint.contains("/login")) return "LOGIN";
            if (endpoint.contains("/register")) return "REGISTER";
            if (endpoint.contains("/logout")) return "LOGOUT";
            return "AUTH";
        }
        
        if (endpoint.startsWith("/api/admin")) {
            if (endpoint.contains("/users")) return "ADMIN_USERS";
            if (endpoint.contains("/logs")) return "ADMIN_LOGS";
            return "ADMIN";
        }
        
        if (endpoint.startsWith("/api/products")) return "PRODUCTS";
        if (endpoint.startsWith("/api/categories")) return "CATEGORIES";
        if (endpoint.startsWith("/api/cart")) return "CART";
        if (endpoint.startsWith("/api/orders")) return "ORDERS";
        
        return method + "_" + endpoint.replaceAll("[^a-zA-Z0-9]", "_").toUpperCase();
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            if ("POST".equals(request.getMethod()) || "PUT".equals(request.getMethod()) || "PATCH".equals(request.getMethod())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                return reader.lines().collect(Collectors.joining());
            }
        } catch (IOException e) {
            // Ignore errors reading request body
        }
        return "";
    }
} 