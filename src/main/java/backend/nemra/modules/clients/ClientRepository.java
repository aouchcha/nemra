package backend.nemra.modules.clients;

import backend.nemra.modules.clients.model.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientProfile, Long> {
    Optional<ClientProfile> findByUser_Id(UUID userId);
}
