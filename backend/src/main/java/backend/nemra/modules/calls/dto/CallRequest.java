package backend.nemra.modules.calls.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CallRequest {
    @NotNull(message = "caller id is required")
    private UUID clientId;
    @NotNull(message = "called id is required")
    private UUID providerId;
}
