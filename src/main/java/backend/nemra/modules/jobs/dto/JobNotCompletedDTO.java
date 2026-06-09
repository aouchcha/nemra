package backend.nemra.modules.jobs.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
@Setter
public class JobNotCompletedDTO extends JobPendingDTO {
    private UUID providerId;
    private String providerName;
}
