package backend.nemra.modules.calls.model;

import backend.nemra.modules.users.clients.model.ClientProfile;
import backend.nemra.modules.users.providers.model.ProviderProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "calls")
@Getter
@Setter
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "caller_id",
            referencedColumnName = "id",
            nullable = false
    )
    private ClientProfile caller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "called_id",
            referencedColumnName = "id",
            nullable = false
    )
    private ProviderProfile called;

    @Column(name = "room_name",  nullable = false)
    private String roomName;

    @Enumerated(EnumType.STRING)
    private CallStatus status = CallStatus.RINGING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "finished_at", updatable = false)
    private LocalDateTime finishedAt;
}
