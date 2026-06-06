package backend.nemra.modules.users.providers.dto;

import backend.nemra.modules.categories.dto.CategoryDTO;
import backend.nemra.modules.users.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Builder
public class ProviderSummaryDTO {
    private UUID id;
    private String fullName;
    private String businessName;
    private CategoryDTO category;
    private double averageRating;
    private boolean isVerified;
}
