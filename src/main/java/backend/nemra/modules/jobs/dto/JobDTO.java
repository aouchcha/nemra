package backend.nemra.modules.jobs.dto;

import backend.nemra.modules.jobs.model.JobStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
public class JobDTO {
    private UUID id;
    private UUID clientId;
    private String clientName;
    private UUID providerId;
    private String providerName;
    private String description;
    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
