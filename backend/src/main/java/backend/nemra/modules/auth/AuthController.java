package backend.nemra.modules.auth;

import backend.nemra.modules.auth.dto.LoginRequest;
import backend.nemra.modules.auth.dto.RegisterClient;
import backend.nemra.modules.auth.dto.RegisterProvider;
import backend.nemra.shared.response.ApiResponse;
import backend.nemra.shared.utils.jwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    //private final jwtUtils jwtUtils;
    public AuthController(
            AuthService authService
           // jwtUtils jwtUtils
    ) {
        this.authService = authService;
      //  this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register/client")
    public ResponseEntity<ApiResponse> registerClient(@Valid @RequestBody RegisterClient registerClient) {
        return authService.registerClient(registerClient);
    }

    @PostMapping("/register/provider")
    public ResponseEntity<ApiResponse> registerProvider(@Valid @ModelAttribute RegisterProvider registerProvider) throws IOException {
        return authService.registerProvider(registerProvider);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@RequestHeader("Authorization") String barerToken) {
        String refreshToken = jwtUtils.extractToken(barerToken);
        return authService.refresh(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String barerToken) {
        String refreshToken = jwtUtils.extractToken(barerToken);
        return authService.logout(refreshToken);
    }
}