package backend.nemra.modules.users.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
@Setter
public class ClientProfileDTO extends UserDTO {
    private UUID client_profile_id;
}
