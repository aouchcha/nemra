package backend.nemra.modules.users.clients.model;

import backend.nemra.modules.users.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "client_profiles")
@Data
public class ClientProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            unique = true,
            nullable = false
    )
    private User user;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;

    private String city;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created = LocalDateTime.now();
}