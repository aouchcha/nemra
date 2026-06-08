package backend.nemra.modules.reviews.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateReviewBaseDTO {
    @NotNull(message = "job id is required")
    private UUID jobId;

    @NotNull(message = "reviewer id is required")
    private UUID reviewerId;

    @NotNull(message = "reviewed id is required")
    private UUID reviewedId;

    @NotNull(message = "reviewer type is required")
    private String reviewerType;

//    @NotNull(message = "Overall rating is required")
//    private int ratingOverall;

    @NotNull(message = "Comment is required")
    private String comment;

    private int ratingQuality;
    private int ratingPunctuality;
    private int ratingCommunication;
    private int ratingPriceFairness;

    private int ratingPayment;
    private int ratingRespect;
}
