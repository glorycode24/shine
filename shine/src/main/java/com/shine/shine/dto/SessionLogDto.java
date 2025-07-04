package com.shine.shine.dto;

import java.time.LocalDateTime;

public class SessionLogDto {
    private Long logId;
    private String userEmail;
    private String userName;
    private String sessionId;
    private String ipAddress;
    private String userAgent;
    private String action;
    private String endpoint;
    private String method;
    private Integer statusCode;
    private LocalDateTime timestamp;
    private Long durationMs;
    private String errorMessage;

    // Constructors
    public SessionLogDto() {}

    public SessionLogDto(Long logId, String userEmail, String userName, String sessionId, 
                        String ipAddress, String userAgent, String action, String endpoint, 
                        String method, Integer statusCode, LocalDateTime timestamp, 
                        Long durationMs, String errorMessage) {
        this.logId = logId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.sessionId = sessionId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.action = action;
        this.endpoint = endpoint;
        this.method = method;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.durationMs = durationMs;
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "SessionLogDto{" +
                "logId=" + logId +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", action='" + action + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", method='" + method + '\'' +
                ", statusCode=" + statusCode +
                ", timestamp=" + timestamp +
                ", durationMs=" + durationMs +
                '}';
    }
} 