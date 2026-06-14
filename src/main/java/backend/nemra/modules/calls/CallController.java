package backend.nemra.modules.calls;

import backend.nemra.modules.calls.dto.CallRequest;
import backend.nemra.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calls")
public class CallController {
    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> createToken(@Valid @RequestBody CallRequest callRequest) {
        return callService.generateToken(callRequest);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> extractData(
            @RequestBody String rawBody,
            @RequestHeader("Authorization") String authHeader
    ) {
        return callService.webhookReceive(rawBody, authHeader);
    }
}
