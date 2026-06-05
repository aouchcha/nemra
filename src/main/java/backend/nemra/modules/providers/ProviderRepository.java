package backend.nemra.modules.providers;

import backend.nemra.modules.providers.model.ProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProviderRepository extends JpaRepository<ProviderProfile, UUID> {
    Optional<ProviderProfile> findByUser_Id(UUID userId);
}
