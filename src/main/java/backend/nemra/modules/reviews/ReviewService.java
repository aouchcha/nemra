package backend.nemra.modules.reviews;

import backend.nemra.modules.jobs.JobRepository;
import backend.nemra.modules.jobs.model.Job;
import backend.nemra.modules.jobs.model.JobStatus;
import backend.nemra.modules.reviews.dto.ClientReviewDTO;
import backend.nemra.modules.reviews.dto.CreateReviewDTO;
import backend.nemra.modules.reviews.dto.ProviderReviewDTO;
import backend.nemra.modules.reviews.model.Review;
import backend.nemra.modules.reviews.model.ReviewerType;
import backend.nemra.modules.users.UserRepository;
import backend.nemra.modules.users.clients.ClientRepository;
import backend.nemra.modules.users.clients.model.ClientProfile;
import backend.nemra.modules.users.model.Role;
import backend.nemra.modules.users.model.User;
import backend.nemra.modules.users.providers.ProviderRepository;
import backend.nemra.modules.users.providers.model.ProviderProfile;
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
    private final ProviderRepository providerRepository;
    private final ClientRepository clientRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            ProviderRepository providerRepository,
            ClientRepository clientRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.providerRepository = providerRepository;
        this.clientRepository = clientRepository;
    }

    public boolean checkIfUserPartOfJob(Job job, User user) {
        if (user.getRole().equals(Role.CLIENT) && !job.getClient().equals(user.getClientProfile())) {
            System.out.println("Job Client Name :" + job.getClient().getFullName());
            System.out.println("User Name :" + user.getClientProfile().getFullName());
            return true;
        }else if (user.getRole().equals(Role.PROVIDER)  && !job.getProvider().equals(user.getProviderProfile())) {
            System.out.println("Job Provider Name :" + job.getProvider().getUser().getFullName());
            System.out.println("User Name :" + user.getProviderProfile().getUser().getFullName());
            return true;
        }
        return false;
    }

    public ResponseEntity<ApiResponse> createReview(CreateReviewDTO request) {
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
        if (!job.getStatus().equals(JobStatus.COMPLETED)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("you can't rate a job not completed", null, false));
        }
        if (checkIfUserPartOfJob(job, user)) {
            System.out.println("Hanni normalement yerje3 Forbiden");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("reviewer not a part of the job", null, false));
        }

        final User reviewed = userRepository.findById(request.getReviewedId()).orElse(null);
        if (reviewed == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("invalid users in the review", null, false));
        }
        if (checkIfUserPartOfJob(job, reviewed)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("reviewed not a part of the job", null, false));
        }
//        if (reviewed.getRole().equals(Role.CLIENT) && !job.getClient().equals(reviewed.getClientProfile())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("invalid client profile", null, false));
//        }else if (reviewed.getRole().equals(Role.PROVIDER)  && !job.getProvider().equals(reviewed.getProviderProfile())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("invalid provider", null, false));
//        }
        Review review = new Review();
        review.setJob(job);
        review.setReviewer(user);
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

    public int calculateOverallRatingForProvider(CreateReviewDTO request) {
        return (request.getRatingQuality() + request.getRatingPunctuality() + request.getRatingCommunication() + request.getRatingPriceFairness()) /4;
    }

    public int calculateOverallRatingForClient(CreateReviewDTO request) {
        return (request.getRatingPayment() + request.getRatingRespect()) / 2;
    }

    public ResponseEntity<ApiResponse> getReviewsOfProvider(UUID providerId) {
        final ProviderProfile provider = providerRepository.findById(providerId).orElse(null);
        if  (provider == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("provider not found", null, false));
        }
        final User user = provider.getUser();
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
        final ClientProfile client = clientRepository.findById(client_id).orElse(null);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("client not found", null, false));
        }
        final User user = client.getUser();
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
