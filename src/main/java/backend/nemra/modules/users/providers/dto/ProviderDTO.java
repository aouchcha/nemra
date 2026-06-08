package backend.nemra.modules.users.providers.dto;

import backend.nemra.modules.categories.dto.CategoryDTO;
import backend.nemra.modules.categories.model.Category;
import backend.nemra.modules.users.dto.UserDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
@Setter
public class ProviderDTO extends UserDTO {
    private UUID providerId;
    private String businessName;
    private CategoryDTO category;
    private String bio;
    private int yearsOfExperience;
    private boolean isVerified;
    private double averageRating;
    private int totalReviews;
    private LocalDateTime createdAt;
}
