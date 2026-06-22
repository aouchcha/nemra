package backend.nemra.modules.jobs.model;

import backend.nemra.modules.users.clients.model.ClientProfile;
import backend.nemra.modules.users.providers.model.ProviderProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "client_id",
            referencedColumnName = "id",
            nullable = false
    )
    private ClientProfile client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "provider_id",
            referencedColumnName = "id",
            nullable = false
    )
    private ProviderProfile provider;

    private String description;

    @Enumerated(EnumType.STRING)
    private JobStatus status =  JobStatus.PENDING;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
