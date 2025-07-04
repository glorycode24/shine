package com.shine.shine.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Service.JwtService;
import com.shine.shine.Service.SessionLogService;
import com.shine.shine.Service.UserService;
import com.shine.shine.dto.SessionLogDto;
import com.shine.shine.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin") // All endpoints here will start with /api/admin
public class AdminController {

    private final UserService userService;
    private final SessionLogService sessionLogService;
    private final JwtService jwtService;

    public AdminController(UserService userService, SessionLogService sessionLogService, JwtService jwtService) {
        this.userService = userService;
        this.sessionLogService = sessionLogService;
        this.jwtService = jwtService;
    }

    // Endpoint to get a list of all users
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllUsersAsDto();
        return ResponseEntity.ok(users);
    }

    // Endpoint to delete a user by their ID
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.noContent().build(); // Standard response for successful delete
        } else {
            return ResponseEntity.notFound().build(); // If the user didn't exist
        }
    }

    // Endpoint to log activity from frontend
    @PostMapping("/logs")
    public ResponseEntity<Object> logActivity(@RequestBody Map<String, Object> activityData) {
        try {
            // This endpoint is for frontend activity logging
            // The actual logging is handled by the LoggingInterceptor automatically
            return ResponseEntity.ok(Map.of(
                "message", "Activity logged successfully",
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "message", "Failed to log activity"
            ));
        }
    }

    // Endpoint to get session logs with pagination
    @GetMapping("/logs")
    public ResponseEntity<List<SessionLogDto>> getSessionLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) Integer statusCode,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        // If userEmail is provided, filter by user
        if (userEmail != null && !userEmail.trim().isEmpty()) {
            Page<SessionLogDto> pageResult = sessionLogService.getSessionLogsByUserEmail(userEmail, pageable);
            return ResponseEntity.ok(pageResult.getContent());
        }
        
        // If action is provided, filter by action
        if (action != null && !action.trim().isEmpty()) {
            List<SessionLogDto> logs = sessionLogService.getSessionLogsByAction(action);
            return ResponseEntity.ok(logs);
        }
        
        // If statusCode is provided, filter by status code
        if (statusCode != null) {
            List<SessionLogDto> logs = sessionLogService.getSessionLogsByStatusCode(statusCode);
            return ResponseEntity.ok(logs);
        }
        
        // If date range is provided, filter by time range
        if (startDate != null && endDate != null) {
            try {
                LocalDateTime start = LocalDateTime.parse(startDate);
                LocalDateTime end = LocalDateTime.parse(endDate);
                List<SessionLogDto> logs = sessionLogService.getSessionLogsByTimeRange(start, end);
                return ResponseEntity.ok(logs);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        // Default: return all logs with pagination (but return content as array)
        Page<SessionLogDto> pageResult = sessionLogService.getAllSessionLogs(pageable);
        return ResponseEntity.ok(pageResult.getContent());
    }

    // Endpoint to get recent session logs
    @GetMapping("/logs/recent")
    public ResponseEntity<List<SessionLogDto>> getRecentSessionLogs(
            @RequestParam(defaultValue = "50") int limit) {
        List<SessionLogDto> logs = sessionLogService.getRecentSessionLogs(limit);
        return ResponseEntity.ok(logs);
    }

    // Endpoint to get error logs
    @GetMapping("/logs/errors")
    public ResponseEntity<List<SessionLogDto>> getErrorLogs(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        if (startDate != null && endDate != null) {
            try {
                LocalDateTime start = LocalDateTime.parse(startDate);
                LocalDateTime end = LocalDateTime.parse(endDate);
                List<SessionLogDto> logs = sessionLogService.getErrorLogsByTimeRange(start, end);
                return ResponseEntity.ok(logs);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        List<SessionLogDto> logs = sessionLogService.getErrorLogs();
        return ResponseEntity.ok(logs);
    }

    // Endpoint to get session logs by user email
    @GetMapping("/logs/user/{email}")
    public ResponseEntity<List<SessionLogDto>> getSessionLogsByUserEmail(@PathVariable String email) {
        List<SessionLogDto> logs = sessionLogService.getSessionLogsByUserEmail(email);
        return ResponseEntity.ok(logs);
    }

    // Endpoint to get session logs by session ID
    @GetMapping("/logs/session/{sessionId}")
    public ResponseEntity<List<SessionLogDto>> getSessionLogsBySessionId(@PathVariable String sessionId) {
        List<SessionLogDto> logs = sessionLogService.getSessionLogsBySessionId(sessionId);
        return ResponseEntity.ok(logs);
    }

    // Endpoint to get session logs by IP address
    @GetMapping("/logs/ip/{ipAddress}")
    public ResponseEntity<List<SessionLogDto>> getSessionLogsByIpAddress(@PathVariable String ipAddress) {
        List<SessionLogDto> logs = sessionLogService.getSessionLogsByIpAddress(ipAddress);
        return ResponseEntity.ok(logs);
    }

    // Endpoint to get session log statistics
    @GetMapping("/logs/stats")
    public ResponseEntity<Object> getSessionLogStats() {
        long totalLogs = sessionLogService.countTotalSessionLogs();
        long errorLogs = sessionLogService.countErrorLogs();
        
        // You can add more statistics here as needed
        return ResponseEntity.ok(Map.of(
            "totalLogs", totalLogs,
            "errorLogs", errorLogs,
            "successRate", totalLogs > 0 ? ((double)(totalLogs - errorLogs) / totalLogs) * 100 : 0
        ));
    }

    // Endpoint to check current user info (for debugging)
    @GetMapping("/user-info")
    public ResponseEntity<Object> getCurrentUserInfo() {
        try {
            UserDto currentUser = userService.getCurrentUserProfile();
            return ResponseEntity.ok(Map.of(
                "user", currentUser,
                "message", "Current user info retrieved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "error", e.getMessage(),
                "message", "Failed to get current user info"
            ));
        }
    }

    // Endpoint to check JWT token info (for debugging)
    @GetMapping("/token-info")
    public ResponseEntity<Object> getTokenInfo(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);
                Integer userId = jwtService.extractUserId(token);
                
                return ResponseEntity.ok(Map.of(
                    "username", username,
                    "role", role,
                    "userId", userId,
                    "message", "Token info extracted successfully"
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "error", "No valid Authorization header found",
                    "message", "Please provide a valid Bearer token"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "error", e.getMessage(),
                "message", "Failed to extract token info"
            ));
        }
    }
}