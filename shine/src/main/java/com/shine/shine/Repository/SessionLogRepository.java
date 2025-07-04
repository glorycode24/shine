package com.shine.shine.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shine.shine.Entity.SessionLog;
import com.shine.shine.Entity.Users;

@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, Long> {

    // Find logs by user
    List<SessionLog> findByUserOrderByTimestampDesc(Users user);
    
    // Find logs by user with pagination
    Page<SessionLog> findByUserOrderByTimestampDesc(Users user, Pageable pageable);
    
    // Find logs by session ID
    List<SessionLog> findBySessionIdOrderByTimestampDesc(String sessionId);
    
    // Find logs by IP address
    List<SessionLog> findByIpAddressOrderByTimestampDesc(String ipAddress);
    
    // Find logs by action
    List<SessionLog> findByActionOrderByTimestampDesc(String action);
    
    // Find logs by status code
    List<SessionLog> findByStatusCodeOrderByTimestampDesc(Integer statusCode);
    
    // Find logs within a time range
    List<SessionLog> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);
    
    // Find logs by user within a time range
    List<SessionLog> findByUserAndTimestampBetweenOrderByTimestampDesc(Users user, LocalDateTime start, LocalDateTime end);
    
    // Find logs with errors (non-2xx status codes)
    List<SessionLog> findByStatusCodeNotOrderByTimestampDesc(Integer statusCode);
    
    // Find logs with errors (status codes >= 400)
    @Query("SELECT sl FROM SessionLog sl WHERE sl.statusCode >= 400 ORDER BY sl.timestamp DESC")
    List<SessionLog> findErrorLogsOrderByTimestampDesc();
    
    // Find logs with errors within a time range
    @Query("SELECT sl FROM SessionLog sl WHERE sl.statusCode >= 400 AND sl.timestamp BETWEEN :start AND :end ORDER BY sl.timestamp DESC")
    List<SessionLog> findErrorLogsBetweenOrderByTimestampDesc(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    // Get recent logs with limit
    @Query("SELECT sl FROM SessionLog sl ORDER BY sl.timestamp DESC")
    List<SessionLog> findRecentLogs(Pageable pageable);
    
    // Get logs by user email
    @Query("SELECT sl FROM SessionLog sl WHERE sl.user.email = :email ORDER BY sl.timestamp DESC")
    List<SessionLog> findByUserEmailOrderByTimestampDesc(@Param("email") String email);
    
    // Get logs by user email with pagination
    @Query("SELECT sl FROM SessionLog sl WHERE sl.user.email = :email ORDER BY sl.timestamp DESC")
    Page<SessionLog> findByUserEmailOrderByTimestampDesc(@Param("email") String email, Pageable pageable);
    
    // Count logs by user
    long countByUser(Users user);
    
    // Count logs by action
    long countByAction(String action);
    
    // Count logs by status code
    long countByStatusCode(Integer statusCode);
    
    // Count logs within a time range
    long countByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    // Count error logs
    @Query("SELECT COUNT(sl) FROM SessionLog sl WHERE sl.statusCode >= 400")
    long countErrorLogs();
    
    // Count error logs within a time range
    @Query("SELECT COUNT(sl) FROM SessionLog sl WHERE sl.statusCode >= 400 AND sl.timestamp BETWEEN :start AND :end")
    long countErrorLogsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
} 