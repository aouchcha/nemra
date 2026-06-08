package backend.nemra.modules.reviews.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ClientReviewDTO extends ReviewResponseDTO {
    private int ratingPayment;
    private int ratingRespect;
}
