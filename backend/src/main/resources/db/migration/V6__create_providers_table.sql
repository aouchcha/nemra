CREATE TABLE provider_profiles (
       id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       user_id           UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
       business_name     VARCHAR(255) NOT NULL,
       category_id       UUID REFERENCES categories(id),
       bio               TEXT,
       years_experience  INT,
       city              VARCHAR(100),
       avatar_url        VARCHAR(500),
       is_verified       BOOLEAN DEFAULT FALSE,
       avg_rating        DECIMAL(3,2) DEFAULT 0.0,
       total_reviews     INT DEFAULT 0,
       created_at        TIMESTAMP DEFAULT NOW()
);