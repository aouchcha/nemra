package backend.nemra.modules.categories.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CategoryDTO {
    private UUID id;
    private String nameEn;
    private String nameFr;
    private String nameAr;
    private boolean isActive;
    private LocalDateTime createdAt;
}
