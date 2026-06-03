package backend.nemra.modules.clients;

import backend.nemra.modules.clients.model.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientProfile, Long> {
}
