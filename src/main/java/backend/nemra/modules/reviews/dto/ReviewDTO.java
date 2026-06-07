package backend.nemra.modules.reviews.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Builder
public class ReviewDTO {
    private UUID id;
    private String reviewerName;
    private String reviewedName;
    private int ratingOverall;
    private LocalDateTime createdAt;
}
