package backend.nemra.modules.jobs.dto;

import backend.nemra.modules.jobs.model.JobStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter
public class JobPendingDTO {
    private UUID id;
    private UUID clientId;
    private String clientName;
    private String description;
    private JobStatus status;
    private LocalDateTime createdAt;
}
