-- Change the foreign key references
ALTER TABLE jobs
DROP CONSTRAINT jobs_client_id_fkey,
DROP CONSTRAINT jobs_provider_id_fkey;

ALTER TABLE jobs
ADD CONSTRAINT jobs_client_id_fkey FOREIGN KEY (client_id) REFERENCES client_profiles(id),
ADD CONSTRAINT jobs_provider_id_fkey FOREIGN KEY (provider_id) REFERENCES provider_profiles(id);

-- Add constraint: client_id must not be null on creation
ALTER TABLE jobs
ADD CONSTRAINT client_required CHECK (client_id IS NOT NULL);