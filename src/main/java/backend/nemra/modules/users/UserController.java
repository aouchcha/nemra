package backend.nemra.modules.users;

import backend.nemra.modules.auth.dto.RegisterRequest;
import backend.nemra.modules.users.dto.UserDTO;
import backend.nemra.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser() {
        return userService.getMe();
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateCurrentUser(@Valid @RequestBody RegisterRequest  registerRequest) {
        return userService.updateMe(registerRequest);
    }
}