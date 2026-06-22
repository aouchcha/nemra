package backend.nemra.shared.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
    private boolean success;
}
