package backend.nemra.modules.jobs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
public class CreateJobRequest {
//    @NotNull(message = "client id is required")
//    private UUID clientId;

//    @NotNull(message = "provider id is required")
//    private UUID providerId;

    @NotBlank(message = "job description is required")
    @Length(min = 10, max = 1000)
    private String description;
}
