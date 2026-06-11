package backend.nemra.shared.utils;

import backend.nemra.modules.categories.dto.CategoryDTO;
import backend.nemra.modules.categories.model.Category;
import backend.nemra.modules.jobs.dto.JobCompletedDTO;
import backend.nemra.modules.jobs.dto.JobNotCompletedDTO;
import backend.nemra.modules.jobs.dto.JobPendingDTO;
import backend.nemra.modules.jobs.model.Job;
import backend.nemra.modules.reviews.dto.ClientReviewDTO;
import backend.nemra.modules.reviews.dto.ProviderReviewDTO;
import backend.nemra.modules.reviews.dto.ReviewResponseDTO;
import backend.nemra.modules.reviews.model.Review;
import backend.nemra.modules.users.clients.model.ClientProfile;
import backend.nemra.modules.users.model.User;
import backend.nemra.modules.users.providers.dto.ProviderDTO;
import backend.nemra.modules.users.providers.dto.ProviderSummaryDTO;
import backend.nemra.modules.users.providers.model.ProviderProfile;
import backend.nemra.modules.users.clients.dto.ClientProfileDTO;
import backend.nemra.modules.users.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;

public class MapperToDTO {

    public static ProviderDTO buildProviderDTO(ProviderProfile providerProfile) {
        return  ProviderDTO.builder()
                .providerId(providerProfile.getId())
                .user_id(providerProfile.getUser().getId())
                .role(providerProfile.getUser().getRole())
                .fullName(providerProfile.getUser().getFullName())
                .businessName(providerProfile.getBusinessName())
                .category(toCategoryDTO(providerProfile.getCategory()))
                .phone(providerProfile.getUser().getPhoneNumber())
                .bio(providerProfile.getBio())
                .yearsOfExperience(providerProfile.getYearsOfExperience())
                .city(providerProfile.getCity())
                .isVerified(providerProfile.is_verified())
                .averageRating(providerProfile.getAvg_rating())
                .totalReviews(providerProfile.getTotal_reviews())
                .createdAt(providerProfile.getCreated_at())
                .avatarUrl(providerProfile.getAvatarUrl())
                .build();
    }

    public static ClientProfileDTO buildClientDTO(ClientProfile clientProfile) {
        return ClientProfileDTO.builder()
                .user_id(clientProfile.getUser().getId())
                .client_profile_id(clientProfile.getId())
                .role(clientProfile.getUser().getRole())
                .phone(clientProfile.getUser().getPhoneNumber())
                .fullName(clientProfile.getFullName())
                .city(clientProfile.getCity())
                .createdAt(clientProfile.getCreated())
                .build();
    }

    public static UserDTO buildUserDTO(User user) {
        return UserDTO.builder()
                .user_id(user.getId())
                .role(user.getRole())
                .fullName(user.getFullName())
                .phone(user.getPhoneNumber())
                .city(user.getCity())
                .createdAt(user.getCreatedAt())
                .build();
    }
    public static CategoryDTO toCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .nameAr(category.getNameAr())
                .nameFr(category.getNameFr())
                .nameEn(category.getNameEn())
                .isActive(category.isActive())
                .createdAt(category.getCreatedAt())
                .build();
    }

    public static ProviderSummaryDTO toProviderSummaryDTO(ProviderProfile providerProfile, String publicURL) {
        return ProviderSummaryDTO.builder()
                .id(providerProfile.getId())
                .fullName(providerProfile.getUser().getFullName())
                .businessName(providerProfile.getBusinessName())
                .category(toCategoryDTO(providerProfile.getCategory()))
                .averageRating(providerProfile.getAvg_rating())
                .isVerified(providerProfile.is_verified())
                .avatarUrl(publicURL + providerProfile.getAvatarUrl())
                .build();
    }

    public static ReviewResponseDTO buildReviewDTO(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .reviewerName(review.getReviewer().getFullName())
                .reviewedName(review.getReviewed().getFullName())
                .comment(review.getComment())
                .ratingOverall(review.getRatingOverall())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static JobPendingDTO buildJobDTO(Job job) {
        JobPendingDTO dto;
        switch (job.getStatus().toString()) {
            case "PENDING":
                dto = JobPendingDTO.builder()
                        .id(job.getId())
                        .clientId(job.getClient().getId())
                        .clientName(job.getClient().getFullName())
                        .description(job.getDescription())
                        .status(job.getStatus())
                        .createdAt(job.getCreatedAt())
                        .build();
                break;
            case "COMPLETED":
                dto = JobCompletedDTO.builder()
                        .id(job.getId())
                        .clientId(job.getClient().getId())
                        .clientName(job.getClient().getFullName())
                        .providerId(job.getProvider().getId())
                        .providerName(job.getProvider().getUser().getFullName())
                        .description(job.getDescription())
                        .status(job.getStatus())
                        .createdAt(job.getCreatedAt())
                        .completedAt(job.getCompletedAt())
                        .build();
                break;
            case "CANCELLED":
                if (job.getProvider() == null) {
                    dto = JobPendingDTO.builder()
                            .id(job.getId())
                            .clientId(job.getClient().getId())
                            .clientName(job.getClient().getFullName())
                            .description(job.getDescription())
                            .status(job.getStatus())
                            .createdAt(job.getCreatedAt())
                            .build();
                    break;
                }
            default:
                dto = JobNotCompletedDTO.builder()
                        .id(job.getId())
                        .clientId(job.getClient().getId())
                        .clientName(job.getClient().getFullName())
                        .providerId(job.getProvider().getId())
                        .providerName(job.getProvider().getUser().getFullName())
                        .description(job.getDescription())
                        .status(job.getStatus())
                        .createdAt(job.getCreatedAt())
                        .build();
        }
        return dto;
    }

    public static ClientReviewDTO buildClientReviewDTO(Review review) {
        return ClientReviewDTO.builder()
                .id(review.getId())
                .reviewerName(review.getReviewer().getFullName())
                .reviewedName(review.getReviewed().getFullName())
                .ratingOverall(review.getRatingOverall())
                .ratingPayment(review.getRatingPayment())
                .ratingRespect(review.getRatingRespect())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static ProviderReviewDTO buildProviderReviewDTO(Review review) {
        return ProviderReviewDTO.builder()
                .id(review.getId())
                .reviewerName(review.getReviewer().getFullName())
                .reviewedName(review.getReviewed().getFullName())
                .ratingOverall(review.getRatingOverall())
                .ratingQuality(review.getRatingQuality())
                .ratingPunctuality(review.getRatingPunctuality())
                .ratingCommunication(review.getRatingCommunication())
                .ratingPriceFairness(review.getRatingPriceFairness())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
