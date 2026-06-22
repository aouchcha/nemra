package backend.nemra.modules.calls.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class CallResponse {
    private String token;
    private String url;
}
