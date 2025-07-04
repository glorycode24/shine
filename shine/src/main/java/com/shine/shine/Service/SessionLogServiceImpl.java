package com.shine.shine.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shine.shine.Entity.SessionLog;
import com.shine.shine.Entity.Users;
import com.shine.shine.Repository.SessionLogRepository;
import com.shine.shine.dto.SessionLogDto;

@Service
public class SessionLogServiceImpl implements SessionLogService {

    private final SessionLogRepository sessionLogRepository;

    public SessionLogServiceImpl(SessionLogRepository sessionLogRepository) {
        this.sessionLogRepository = sessionLogRepository;
    }

    @Override
    public SessionLog saveSessionLog(SessionLog sessionLog) {
        return sessionLogRepository.save(sessionLog);
    }

    @Override
    public SessionLog getSessionLogById(Long logId) {
        return sessionLogRepository.findById(logId).orElse(null);
    }

    @Override
    public Page<SessionLogDto> getAllSessionLogs(Pageable pageable) {
        Page<SessionLog> sessionLogs = sessionLogRepository.findAll(pageable);
        return sessionLogs.map(this::convertToDto);
    }

    @Override
    public List<SessionLogDto> getRecentSessionLogs(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<SessionLog> sessionLogs = sessionLogRepository.findRecentLogs(pageable);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<SessionLogDto> getSessionLogsByUser(Users user) {
        List<SessionLog> sessionLogs = sessionLogRepository.findByUserOrderByTimestampDesc(user);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Page<SessionLogDto> getSessionLogsByUser(Users user, Pageable pageable) {
        Page<SessionLog> sessionLogs = sessionLogRepository.findByUserOrderByTimestampDesc(user, pageable);
        return sessionLogs.map(this::convertToDto);
    }

    @Override
    public List<SessionLogDto> getSessionLogsByUserEmail(String email) {
        List<SessionLog> sessionLogs = sessionLogRepository.findByUserEmailOrderByTimestampDesc(email);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Page<SessionLogDto> getSessionLogsByUserEmail(String email, Pageable pageable) {
        Page<SessionLog> sessionLogs = sessionLogRepository.findByUserEmailOrderByTimestampDesc(email, pageable);
        return sessionLogs.map(this::convertToDto);
    }

    @Override
    public List<SessionLogDto> getSessionLogsBySessionId(String sessionId) {
        List<SessionLog> sessionLogs = sessionLogRepository.findBySessionIdOrderByTimestampDesc(sessionId);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<SessionLogDto> getSessionLogsByIpAddress(String ipAddress) {
        List<SessionLog> sessionLogs = sessionLogRepository.findByIpAddressOrderByTimestampDesc(ipAddress);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<SessionLogDto> getSessionLogsByAction(String action) {
        List<SessionLog> sessionLogs = sessionLogRepository.findByActionOrderByTimestampDesc(action);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<SessionLogDto> getSessionLogsByStatusCode(Integer statusCode) {
        List<SessionLog> sessionLogs = sessionLogRepository.findByStatusCodeOrderByTimestampDesc(statusCode);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<SessionLogDto> getSessionLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        List<SessionLog> sessionLogs = sessionLogRepository.findByTimestampBetweenOrderByTimestampDesc(start, end);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<SessionLogDto> getSessionLogsByUserAndTimeRange(Users user, LocalDateTime start, LocalDateTime end) {
        List<SessionLog> sessionLogs = sessionLogRepository.findByUserAndTimestampBetweenOrderByTimestampDesc(user, start, end);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<SessionLogDto> getErrorLogs() {
        List<SessionLog> sessionLogs = sessionLogRepository.findErrorLogsOrderByTimestampDesc();
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<SessionLogDto> getErrorLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        List<SessionLog> sessionLogs = sessionLogRepository.findErrorLogsBetweenOrderByTimestampDesc(start, end);
        return sessionLogs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public long countTotalSessionLogs() {
        return sessionLogRepository.count();
    }

    @Override
    public long countSessionLogsByUser(Users user) {
        return sessionLogRepository.countByUser(user);
    }

    @Override
    public long countSessionLogsByAction(String action) {
        return sessionLogRepository.countByAction(action);
    }

    @Override
    public long countSessionLogsByStatusCode(Integer statusCode) {
        return sessionLogRepository.countByStatusCode(statusCode);
    }

    @Override
    public long countSessionLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return sessionLogRepository.countByTimestampBetween(start, end);
    }

    @Override
    public long countErrorLogs() {
        return sessionLogRepository.countErrorLogs();
    }

    @Override
    public long countErrorLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return sessionLogRepository.countErrorLogsBetween(start, end);
    }

    @Override
    public void deleteOldSessionLogs(LocalDateTime cutoffDate) {
        // This would need a custom query in the repository
        // For now, we'll implement a simple approach
        List<SessionLog> oldLogs = sessionLogRepository.findByTimestampBetweenOrderByTimestampDesc(
            LocalDateTime.MIN, cutoffDate);
        sessionLogRepository.deleteAll(oldLogs);
    }

    @Override
    public void deleteSessionLogsByUser(Users user) {
        List<SessionLog> userLogs = sessionLogRepository.findByUserOrderByTimestampDesc(user);
        sessionLogRepository.deleteAll(userLogs);
    }

    @Override
    public void deleteSessionLogById(Long logId) {
        sessionLogRepository.deleteById(logId);
    }

    // Helper method to convert SessionLog entity to SessionLogDto
    private SessionLogDto convertToDto(SessionLog sessionLog) {
        String userEmail = sessionLog.getUser() != null ? sessionLog.getUser().getEmail() : null;
        String userName = sessionLog.getUser() != null ? 
            sessionLog.getUser().getFirstName() + " " + sessionLog.getUser().getLastName() : null;
        
        return new SessionLogDto(
            sessionLog.getLogId(),
            userEmail,
            userName,
            sessionLog.getSessionId(),
            sessionLog.getIpAddress(),
            sessionLog.getUserAgent(),
            sessionLog.getAction(),
            sessionLog.getEndpoint(),
            sessionLog.getMethod(),
            sessionLog.getStatusCode(),
            sessionLog.getTimestamp(),
            sessionLog.getDurationMs(),
            sessionLog.getErrorMessage()
        );
    }
} 