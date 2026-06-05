package backend.nemra.modules.auth;

import backend.nemra.modules.auth.model.RefreshToken;
import backend.nemra.modules.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<RefreshToken, UUID> {
    boolean existsByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByRefreshTokenAndUser_Id(String refreshToken, UUID user_id);

    Optional<RefreshToken> findByUser_Id(UUID userId);
}
