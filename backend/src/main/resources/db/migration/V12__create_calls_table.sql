CREATE TABLE calls (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    caller_id UUID REFERENCES client_profiles(id),
    called_id UUID REFERENCES provider_profiles(id),
    room_name TEXT NOT NULL,
    status        VARCHAR(20) DEFAULT 'RINGING'
        CHECK (status IN ('RINGING','ACCEPTED','FINISHED','REFUSED')),
    created_at TIMESTAMP DEFAULT NOW(),
    finished_at TIMESTAMP
);