package backend.nemra.modules.auth.model;

import backend.nemra.modules.users.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Data
@RequiredArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false,
            unique = true
    )
    private User user;

    @Column(nullable = false, unique = true, name = "token")
    private String refreshToken;

    private LocalDateTime createdAt =  LocalDateTime.now();
}
