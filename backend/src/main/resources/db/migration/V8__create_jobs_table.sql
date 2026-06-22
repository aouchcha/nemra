CREATE TABLE jobs (
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  client_id     UUID REFERENCES users(id),
  provider_id   UUID REFERENCES users(id),
  description   TEXT,
  status        VARCHAR(20) DEFAULT 'PENDING'
      CHECK (status IN ('PENDING','ACCEPTED','COMPLETED','CANCELLED')),
  created_at    TIMESTAMP DEFAULT NOW(),
  completed_at  TIMESTAMP
);