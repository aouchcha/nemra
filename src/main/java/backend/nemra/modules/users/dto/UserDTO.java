package backend.nemra.modules.users.dto;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@SuperBuilder
public class UserDTO {
    private UUID user_id;
    private String fullName;
    private String city;
    private Date createdAt;
}