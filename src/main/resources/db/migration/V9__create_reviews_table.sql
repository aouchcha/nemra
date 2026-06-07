CREATE TABLE reviews (
     id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     job_id                UUID REFERENCES jobs(id),
     reviewer_id           UUID REFERENCES users(id),
     reviewed_id           UUID REFERENCES users(id),
     reviewer_type         VARCHAR(20) CHECK (reviewer_type IN ('CLIENT', 'PROVIDER')),

    -- Overall score
     rating_overall        SMALLINT CHECK (rating_overall BETWEEN 1 AND 5),

    -- Provider-specific sub-ratings (filled when CLIENT reviews PROVIDER)
     rating_quality        SMALLINT CHECK (rating_quality BETWEEN 1 AND 5),
     rating_punctuality    SMALLINT CHECK (rating_punctuality BETWEEN 1 AND 5),
     rating_communication  SMALLINT CHECK (rating_communication BETWEEN 1 AND 5),
     rating_price_fairness SMALLINT CHECK (rating_price_fairness BETWEEN 1 AND 5),

    -- Client-specific sub-ratings (filled when PROVIDER reviews CLIENT)
     rating_payment        SMALLINT CHECK (rating_payment BETWEEN 1 AND 5),
     rating_respect        SMALLINT CHECK (rating_respect BETWEEN 1 AND 5),

     comment               TEXT,
     created_at            TIMESTAMP DEFAULT NOW(),
    
     CONSTRAINT unique_review_per_job_per_type UNIQUE (job_id, reviewer_type)
);