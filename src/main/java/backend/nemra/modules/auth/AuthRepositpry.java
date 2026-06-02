package backend.nemra.modules.auth;

import backend.nemra.modules.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthRepositpry extends JpaRepository<User, UUID> {
}