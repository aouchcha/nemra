package backend.nemra.modules.users.clients;

import backend.nemra.modules.users.clients.model.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientProfile, UUID> {
    Optional<ClientProfile> findByUser_Id(UUID userId);
}
