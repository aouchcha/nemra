package backend.nemra.modules.users.dto;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
public class ClientProfileDTO extends UserDTO {
    private UUID client_profile_id;
}
