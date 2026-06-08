package backend.nemra.modules.reviews;

import backend.nemra.modules.jobs.JobRepository;
import backend.nemra.modules.jobs.model.Job;
import backend.nemra.modules.reviews.dto.ClientReviewDTO;
import backend.nemra.modules.reviews.dto.CreateReviewBaseDTO;
import backend.nemra.modules.reviews.dto.ProviderReviewDTO;
import backend.nemra.modules.reviews.model.Review;
import backend.nemra.modules.reviews.model.ReviewerType;
import backend.nemra.modules.users.UserRepository;
import backend.nemra.modules.users.model.Role;
import backend.nemra.modules.users.model.User;
import backend.nemra.shared.response.ApiResponse;
import backend.nemra.shared.utils.MapperToDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            JobRepository jobRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public ResponseEntity<ApiResponse> createReview(CreateReviewBaseDTO request) {
        final UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        final User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("user not found", null, false));
        }
        if (!user.getRole().toString().equals(request.getReviewerType())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("invalid reviewer", null, false));
        }
        final Job job = jobRepository.findById(request.getJobId()).orElse(null);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("invalid job", null, false));
        }
        final User reviewer =  userRepository.findById(request.getReviewerId()).orElse(null);
        final User reviewed = userRepository.findById(request.getReviewedId()).orElse(null);
        if (reviewer == null || reviewed == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("invalid users in the review", null, false));
        }
        Review review = new Review();
        review.setJob(job);
        review.setReviewer(reviewer);
        review.setReviewed(reviewed);
        switch(request.getReviewerType()) {
            case "CLIENT":
                review.setReviewerType(ReviewerType.CLIENT);
                review.setRatingQuality(request.getRatingQuality());
                review.setRatingPunctuality(request.getRatingPunctuality());
                review.setRatingCommunication(request.getRatingCommunication());
                review.setRatingPriceFairness(request.getRatingPriceFairness());
                review.setRatingOverall(calculateOverallRatingForProvider(request));
                break;
            case "PROVIDER":
                review.setReviewerType(ReviewerType.PROVIDER);
                review.setRatingPayment(request.getRatingPayment());
                review.setRatingRespect(request.getRatingRespect());
                review.setRatingOverall(calculateOverallRatingForClient(request));
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("invalid reviewer", null, false));
        }
        review.setComment(request.getComment());
        review = reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("review created", MapperToDTO.buildReviewDTO(review), true));
    }

    public int calculateOverallRatingForProvider(CreateReviewBaseDTO request) {
        return (request.getRatingQuality() + request.getRatingPunctuality() + request.getRatingCommunication() + request.getRatingPriceFairness()) /4;
    }

    public int calculateOverallRatingForClient(CreateReviewBaseDTO request) {
        return (request.getRatingPayment() + request.getRatingRespect()) / 2;
    }

    public ResponseEntity<ApiResponse> getReviewsOfProvider(UUID providerId) {
        final User user = userRepository.findById(providerId).orElse(null);
        if  (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("user not found", null, false));
        }
        if (!user.getRole().equals(Role.PROVIDER)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("invalid reviewer", null, false));
        }
        List<ProviderReviewDTO> reviews = user.getReviewedList().stream()
                .map(MapperToDTO::buildProviderReviewDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("reviews", reviews, true));
    }

    public ResponseEntity<ApiResponse> getReviewsOfClient(UUID client_id) {
        final User user = userRepository.findById(client_id).orElse(null);
        if  (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("user not found", null, false));
        }
        if (!user.getRole().equals(Role.CLIENT)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("invalid reviewer", null, false));
        }
        List<ClientReviewDTO> reviews = user.getReviewedList().stream()
                .map(MapperToDTO::buildClientReviewDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("reviews", reviews, true));
    }
}
