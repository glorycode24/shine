-- Create indexes for session_logs table
-- Note: These commands will fail if indexes already exist, which is fine

-- Single column indexes
CREATE INDEX idx_session_logs_user_id ON session_logs(user_id);
CREATE INDEX idx_session_logs_session_id ON session_logs(session_id);
CREATE INDEX idx_session_logs_timestamp ON session_logs(timestamp);
CREATE INDEX idx_session_logs_action ON session_logs(action);
CREATE INDEX idx_session_logs_status_code ON session_logs(status_code);
CREATE INDEX idx_session_logs_ip_address ON session_logs(ip_address);

-- Composite indexes for common queries
CREATE INDEX idx_session_logs_user_timestamp ON session_logs(user_id, timestamp);
CREATE INDEX idx_session_logs_session_timestamp ON session_logs(session_id, timestamp); 