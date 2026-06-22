package backend.nemra.modules.users.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String phoneNumber;
    private String password;
    private String city;
}