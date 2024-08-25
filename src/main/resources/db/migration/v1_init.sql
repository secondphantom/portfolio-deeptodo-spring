-- Table for users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    nick_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(600) NOT NULL,
    billing_key VARCHAR(255) NOT NULL,
    oauth_server_id VARCHAR(255) NOT NULL,
    oauth_server_type VARCHAR(50) NOT NULL,
    oauth_token VARCHAR(255) NOT NULL
);

-- Table for subscriptionplans
CREATE TABLE subscriptionplans (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    duration_days INT NOT NULL,
    max_project_count INT NOT NULL,
    max_todo_count INT NOT NULL,
    enabled BOOLEAN NOT NULL
);

-- Table for subscriptions
CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    expired_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (plan_id) REFERENCES subscriptionplans (id)
);

-- Table for projects
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    version INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    root JSONB NOT NULL,
    boards JSONB NOT NULL,
    todos JSONB NOT NULL,
    enabled BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Table for payments
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions (id)
);

CREATE INDEX idx_user_oauth_server_id ON users(oauth_server_id);
CREATE UNIQUE INDEX uq_user_email ON users(email);
CREATE INDEX idx_user_created_at_desc ON projects(created_at DESC);
CREATE INDEX idx_project_user_id_created_at_desc ON projects(user_id, created_at DESC);
CREATE INDEX idx_project_user_id_updated_at_desc ON projects(user_id, updated_at DESC);
CREATE INDEX idx_payment_user_id_created_at_desc ON payments(user_id, created_at DESC);
CREATE INDEX idx_subscription_user_id ON subscriptions(user_id); 