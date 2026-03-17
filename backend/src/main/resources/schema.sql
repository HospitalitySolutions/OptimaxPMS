-- Basic user table for authentication
-- Adjust this schema later to match your final auth model
CREATE TABLE IF NOT EXISTS users (
    id           BIGSERIAL PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    role         VARCHAR(50)  NOT NULL DEFAULT 'USER',
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Create default admin user
INSERT INTO users (email, password, role, is_active)
VALUES ('admin@optimax.com', 'password', 'ADMIN', TRUE)
ON CONFLICT (email) DO NOTHING;

