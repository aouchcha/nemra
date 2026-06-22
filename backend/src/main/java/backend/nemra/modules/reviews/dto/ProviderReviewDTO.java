package backend.nemra.modules.reviews.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ProviderReviewDTO extends ReviewResponseDTO{
    private int ratingQuality;
    private int ratingPunctuality;
    private int ratingCommunication;
    private int ratingPriceFairness;
}
