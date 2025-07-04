package com.shine.shine.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shine.shine.Entity.SessionLog;
import com.shine.shine.Entity.Users;
import com.shine.shine.dto.SessionLogDto;

public interface SessionLogService {
    
    // Save a new session log
    SessionLog saveSessionLog(SessionLog sessionLog);
    
    // Get session log by ID
    SessionLog getSessionLogById(Long logId);
    
    // Get all session logs with pagination
    Page<SessionLogDto> getAllSessionLogs(Pageable pageable);
    
    // Get recent session logs with limit
    List<SessionLogDto> getRecentSessionLogs(int limit);
    
    // Get session logs by user
    List<SessionLogDto> getSessionLogsByUser(Users user);
    
    // Get session logs by user with pagination
    Page<SessionLogDto> getSessionLogsByUser(Users user, Pageable pageable);
    
    // Get session logs by user email
    List<SessionLogDto> getSessionLogsByUserEmail(String email);
    
    // Get session logs by user email with pagination
    Page<SessionLogDto> getSessionLogsByUserEmail(String email, Pageable pageable);
    
    // Get session logs by session ID
    List<SessionLogDto> getSessionLogsBySessionId(String sessionId);
    
    // Get session logs by IP address
    List<SessionLogDto> getSessionLogsByIpAddress(String ipAddress);
    
    // Get session logs by action
    List<SessionLogDto> getSessionLogsByAction(String action);
    
    // Get session logs by status code
    List<SessionLogDto> getSessionLogsByStatusCode(Integer statusCode);
    
    // Get session logs within a time range
    List<SessionLogDto> getSessionLogsByTimeRange(LocalDateTime start, LocalDateTime end);
    
    // Get session logs by user within a time range
    List<SessionLogDto> getSessionLogsByUserAndTimeRange(Users user, LocalDateTime start, LocalDateTime end);
    
    // Get error logs (status codes >= 400)
    List<SessionLogDto> getErrorLogs();
    
    // Get error logs within a time range
    List<SessionLogDto> getErrorLogsByTimeRange(LocalDateTime start, LocalDateTime end);
    
    // Count total session logs
    long countTotalSessionLogs();
    
    // Count session logs by user
    long countSessionLogsByUser(Users user);
    
    // Count session logs by action
    long countSessionLogsByAction(String action);
    
    // Count session logs by status code
    long countSessionLogsByStatusCode(Integer statusCode);
    
    // Count session logs within a time range
    long countSessionLogsByTimeRange(LocalDateTime start, LocalDateTime end);
    
    // Count error logs
    long countErrorLogs();
    
    // Count error logs within a time range
    long countErrorLogsByTimeRange(LocalDateTime start, LocalDateTime end);
    
    // Delete session logs older than a certain date
    void deleteOldSessionLogs(LocalDateTime cutoffDate);
    
    // Delete session logs by user
    void deleteSessionLogsByUser(Users user);
    
    // Delete session log by ID
    void deleteSessionLogById(Long logId);
} 