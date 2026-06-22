package backend.nemra.modules.categories.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {
    @NotBlank(message = "Arabic name is required")
    private String nameAr;

    @NotBlank(message = "French name is required")
    private String nameFr;

    @NotBlank(message = "English name is required")
    private String nameEn;
}
