package backend.nemra.modules.jobs;

import backend.nemra.modules.jobs.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    boolean existsByProviderId(UUID providerId);
    boolean existsByClientId(UUID clientId);
    boolean existsByProviderIdOrClientId(UUID providerId, UUID clientId);
}
