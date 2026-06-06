package backend.nemra.shared.utils;

import backend.nemra.modules.categories.dto.CategoryDTO;
import backend.nemra.modules.categories.model.Category;
import backend.nemra.modules.users.clients.model.ClientProfile;
import backend.nemra.modules.users.model.User;
import backend.nemra.modules.users.providers.dto.ProviderDTO;
import backend.nemra.modules.users.providers.dto.ProviderSummaryDTO;
import backend.nemra.modules.users.providers.model.ProviderProfile;
import backend.nemra.modules.users.clients.dto.ClientProfileDTO;
import backend.nemra.modules.users.dto.UserDTO;

public class MapperToDTO {

    public static ProviderDTO buildProviderDTO(ProviderProfile providerProfile) {
        return  ProviderDTO.builder()
                .providerId(providerProfile.getId())
                .user_id(providerProfile.getUser().getId())
                .role(providerProfile.getUser().getRole())
                .businessName(providerProfile.getBusinessName())
                .category(providerProfile.getCategory().getNameAr())
                .phone(providerProfile.getUser().getPhoneNumber())
                .bio(providerProfile.getBio())
                .yearsOfExperience(providerProfile.getYearsOfExperience())
                .city(providerProfile.getCity())
                .isVerified(providerProfile.is_verified())
                .averageRating(providerProfile.getAvg_rating())
                .totalReviews(providerProfile.getTotal_reviews())
                .createdAt(providerProfile.getCreated_at())
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

    public static ProviderSummaryDTO toProviderSummaryDTO(ProviderProfile providerProfile) {
        return ProviderSummaryDTO.builder()
                .id(providerProfile.getId())
                .fullName(providerProfile.getUser().getFullName())
                .businessName(providerProfile.getBusinessName())
                .category(toCategoryDTO(providerProfile.getCategory()))
                .averageRating(providerProfile.getAvg_rating())
                .isVerified(providerProfile.is_verified())
                .build();
    }
}
