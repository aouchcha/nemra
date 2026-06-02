package backend.nemra.modules.users.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "clients_profile")
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
    private Date created;
}