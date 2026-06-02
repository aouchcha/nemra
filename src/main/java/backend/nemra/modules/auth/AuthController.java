package backend.nemra.modules.auth;

import backend.nemra.modules.auth.dto.RegisterClient;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/register/client")
    public void registerClient(@Valid @RequestBody RegisterClient registerClient) {

    }
}