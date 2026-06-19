-- Default admin user (password: admin123)
INSERT INTO users (username, password, email, full_name, role, enabled, created_at, updated_at)
SELECT 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@insurance.com', 'System Admin', 'ROLE_ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Default agent user (password: agent123)
INSERT INTO users (username, password, email, full_name, role, enabled, created_at, updated_at)
SELECT 'agent1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'agent1@insurance.com', 'Agent One', 'ROLE_AGENT', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'agent1');
