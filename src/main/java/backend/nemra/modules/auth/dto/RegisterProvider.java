package backend.nemra.modules.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class RegisterProvider extends RegisterRequest {
    private String business_name;
    private UUID business_id;
    private String bio;
    private int years_of_experience;
    private boolean is_verified = false;
    private double avg_rating = 0.0;
    private int total_reviews = 0;
}
