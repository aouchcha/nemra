CREATE TABLE client_profiles (
     id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     user_id     UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
     full_name   VARCHAR(255) NOT NULL,
     avatar_url  VARCHAR(500),
     city        VARCHAR(100),
     created_at  TIMESTAMP DEFAULT NOW()
);