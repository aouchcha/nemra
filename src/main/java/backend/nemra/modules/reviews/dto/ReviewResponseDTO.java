package backend.nemra.modules.reviews.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@SuperBuilder
public class ReviewResponseDTO {
    private UUID id;
    private String reviewerName;
    private String reviewedName;
    private int ratingOverall;
    private String comment;
    private LocalDateTime createdAt;
}
