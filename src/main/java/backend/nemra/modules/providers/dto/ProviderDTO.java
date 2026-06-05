package backend.nemra.modules.providers.dto;

import backend.nemra.modules.users.dto.UserDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
@Setter
public class ProviderDTO extends UserDTO {
    private UUID providerId;
    private String businessName;
    private String category;
    private String bio;
    private int yearsOfExperience;
    private boolean isVerified;
    private double averageRating;
    private int totalReviews;
    private Date createdAt;
}
