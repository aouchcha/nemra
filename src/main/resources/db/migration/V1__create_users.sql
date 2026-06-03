CREATE TABLE users (
   id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   full_name    VARCHAR(255)  NOT NULL,
   phone         VARCHAR(20) UNIQUE,
   password_hash VARCHAR(255) NOT NULL,
   role          VARCHAR(20) NOT NULL CHECK (role IN ('CLIENT', 'PROVIDER', 'ADMIN')),
   is_active     BOOLEAN NOT NULL DEFAULT TRUE,
   created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
   updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);