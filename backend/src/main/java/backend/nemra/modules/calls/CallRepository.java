package backend.nemra.modules.calls;

import backend.nemra.modules.calls.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CallRepository extends JpaRepository<Call, UUID> {
    Optional<Call> findFirstByRoomNameOrderByCreatedAtDesc(String roomName);
}
