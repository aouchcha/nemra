package backend.nemra.modules.reviews;

import backend.nemra.modules.reviews.dto.CreateReviewDTO;
import backend.nemra.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    public ReviewController(
            ReviewService reviewService
    ) {
        this.reviewService = reviewService;
    }
    @PostMapping()
    public ResponseEntity<ApiResponse> createReview(@Valid @RequestBody CreateReviewDTO review) {
        return reviewService.createReview(review);
    }

    @GetMapping("/provider/{id}")
    public ResponseEntity<ApiResponse> getProviderReviews(@PathVariable UUID id) {
        return reviewService.getReviewsOfProvider(id);
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ApiResponse> getClientReviews(@PathVariable UUID id) {
        return reviewService.getReviewsOfClient(id);
    }
}
