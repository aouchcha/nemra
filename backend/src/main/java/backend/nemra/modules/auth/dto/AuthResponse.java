package backend.nemra.modules.auth.dto;

import backend.nemra.modules.users.dto.UserDTO;
import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
}