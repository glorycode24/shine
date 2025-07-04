# Session Logs Implementation

This document explains how to set up and use the session logs functionality for the admin panel.

## Overview

The session logs system automatically tracks all API requests and responses, providing detailed information about:
- User sessions
- Request/response details
- Performance metrics
- Error tracking
- IP addresses and user agents

## Setup Instructions

### 1. Database Setup

Run the SQL script to create the session_logs table:

```sql
-- Execute the contents of src/main/resources/session_logs.sql
```

### 2. Create Admin User

The system includes a data.sql file that will automatically create test users:

- **Admin User**: admin@shine.com / admin123
- **Regular User**: user@shine.com / user123

### 3. Build and Run

```bash
cd shine
./mvnw clean install
./mvnw spring-boot:run
```

## API Endpoints

### Get Session Logs (Paginated)
```
GET /api/admin/logs?page=0&size=50
```

**Query Parameters:**
- `page` (default: 0) - Page number
- `size` (default: 50) - Page size
- `userEmail` (optional) - Filter by user email
- `action` (optional) - Filter by action
- `statusCode` (optional) - Filter by status code
- `startDate` (optional) - Start date (ISO format)
- `endDate` (optional) - End date (ISO format)

### Get Recent Session Logs
```
GET /api/admin/logs/recent?limit=50
```

### Get Error Logs
```
GET /api/admin/logs/errors?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59
```

### Get Logs by User Email
```
GET /api/admin/logs/user/{email}
```

### Get Logs by Session ID
```
GET /api/admin/logs/session/{sessionId}
```

### Get Logs by IP Address
```
GET /api/admin/logs/ip/{ipAddress}
```

### Get Log Statistics
```
GET /api/admin/logs/stats
```

## Authentication

All admin endpoints require ADMIN role. Make sure your user has the ADMIN role in the database.

## Response Format

Session logs are returned as SessionLogDto objects with the following structure:

```json
{
  "logId": 1,
  "userEmail": "user@example.com",
  "userName": "John Doe",
  "sessionId": "uuid-session-id",
  "ipAddress": "192.168.1.1",
  "userAgent": "Mozilla/5.0...",
  "action": "LOGIN",
  "endpoint": "/api/auth/login",
  "method": "POST",
  "statusCode": 200,
  "timestamp": "2024-01-01T12:00:00",
  "durationMs": 150,
  "errorMessage": null
}
```

## Automatic Logging

The system automatically logs all API requests through the LoggingInterceptor. It captures:

- Request details (method, endpoint, body)
- Response details (status code, duration)
- User information (if authenticated)
- Session tracking
- Error messages

## Security Considerations

1. **Sensitive Data**: Request bodies for auth endpoints are excluded from logging
2. **Data Retention**: Consider implementing a cleanup job for old logs
3. **Access Control**: Only users with ADMIN role can access log endpoints

## Performance

- Indexes are created on commonly queried fields
- Pagination is implemented for large datasets
- Logging is asynchronous to avoid impacting request performance

## Troubleshooting

### 403 Forbidden Error
- Ensure your user has ADMIN role
- Check that the JWT token is valid and includes ADMIN role
- Verify the token is being sent in the Authorization header

### No Logs Appearing
- Check that the session_logs table exists
- Verify the LoggingInterceptor is properly configured
- Check application logs for any errors

### Database Connection Issues
- Ensure your database is running
- Check application.properties for correct database configuration
- Verify the session_logs table was created successfully

## Customization

You can customize the logging behavior by modifying:

1. **LoggingInterceptor**: Change what gets logged
2. **SessionLog entity**: Add new fields
3. **AdminController**: Add new filtering options
4. **SessionLogService**: Add new query methods

## Example Usage

### Frontend Integration

```javascript
// Get recent logs
const response = await fetch('/api/admin/logs/recent?limit=10', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
const logs = await response.json();

// Get logs for specific user
const userLogs = await fetch('/api/admin/logs/user/user@example.com', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

// Get error logs
const errorLogs = await fetch('/api/admin/logs/errors', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

### Filtering Examples

```javascript
// Get logs for today
const today = new Date().toISOString().split('T')[0];
const todayLogs = await fetch(`/api/admin/logs?startDate=${today}T00:00:00&endDate=${today}T23:59:59`);

// Get logs for specific action
const loginLogs = await fetch('/api/admin/logs?action=LOGIN');

// Get error logs
const errorLogs = await fetch('/api/admin/logs?statusCode=500');
``` 