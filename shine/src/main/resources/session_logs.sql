-- Create session_logs table
CREATE TABLE IF NOT EXISTS session_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    session_id VARCHAR(255) NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    action VARCHAR(100),
    endpoint VARCHAR(500),
    method VARCHAR(10),
    status_code INT,
    request_body TEXT,
    response_body TEXT,
    timestamp DATETIME NOT NULL,
    duration_ms BIGINT,
    error_message TEXT,
    FOREIGN KEY (user_id) REFERENCES users(userid) ON DELETE SET NULL
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_session_logs_user_id ON session_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_session_logs_session_id ON session_logs(session_id);
CREATE INDEX IF NOT EXISTS idx_session_logs_timestamp ON session_logs(timestamp);
CREATE INDEX IF NOT EXISTS idx_session_logs_action ON session_logs(action);
CREATE INDEX IF NOT EXISTS idx_session_logs_status_code ON session_logs(status_code);
CREATE INDEX IF NOT EXISTS idx_session_logs_ip_address ON session_logs(ip_address);

-- Create composite index for common queries
CREATE INDEX IF NOT EXISTS idx_session_logs_user_timestamp ON session_logs(user_id, timestamp);
CREATE INDEX IF NOT EXISTS idx_session_logs_session_timestamp ON session_logs(session_id, timestamp); 