package backend.nemra.modules.users.dto;

import backend.nemra.modules.users.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@SuperBuilder
@Getter
public class UserDTO {
    private UUID user_id;
    private String fullName;
    private String phone;
    private Role role;
    private String city;
    private LocalDateTime createdAt;
}