package backend.nemra.modules.jobs.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class JobCompletedDTO extends JobNotCompletedDTO {
    private LocalDateTime completedAt;
}
