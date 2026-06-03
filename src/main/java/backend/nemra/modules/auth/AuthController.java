package backend.nemra.modules.auth;

import backend.nemra.modules.auth.dto.RegisterClient;
import backend.nemra.modules.auth.dto.RegisterProvider;
import backend.nemra.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/client")
    public ResponseEntity<ApiResponse> registerClient(@Valid @RequestBody RegisterClient registerClient) {
        return authService.RegisterClient(registerClient);
    }

    @PostMapping("/register/provider")
    public ResponseEntity<ApiResponse> registerProvider(@Valid @RequestBody RegisterProvider registerProvider) {
        return authService.RegisterProvider(registerProvider);
    }
}