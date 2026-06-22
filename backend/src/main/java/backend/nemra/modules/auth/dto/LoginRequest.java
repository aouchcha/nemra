package backend.nemra.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequest {
    @NotBlank(message = "phone number is required")
    ///@Size(min = 10, max = 13)
    @Length(min = 10, max = 13)
    private String number;

    @NotNull(message = "password is required")
    private String password;
}