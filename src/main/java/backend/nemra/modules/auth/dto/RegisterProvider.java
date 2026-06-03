package backend.nemra.modules.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class RegisterProvider extends RegisterRequest {
    @NotNull(message = "business name is required")
    private String business_name;
    @NotNull(message = "category is required")
    private String category;
    private String bio;
    private int years_of_experience;
}
