package backend.nemra.shared.utils;

import backend.nemra.modules.clients.model.ClientProfile;
import backend.nemra.modules.providers.dto.ProviderDTO;
import backend.nemra.modules.providers.model.ProviderProfile;
import backend.nemra.modules.users.dto.ClientProfileDTO;
import backend.nemra.modules.users.dto.UserDTO;

public class MapperUserDTO {
    private static ProviderDTO provider;
    private static ClientProfileDTO client;

    public static UserDTO buildProviderDTO(ProviderProfile providerProfile) {
        return  ProviderDTO.builder()
                .providerId(providerProfile.getId())
                .user_id(providerProfile.getUser().getId())
                .businessName(providerProfile.getBusinessName())
                .category(providerProfile.getCategory().getNameAr())
                .bio(providerProfile.getBio())
                .yearsOfExperience(providerProfile.getYearsOfExperience())
                .city(providerProfile.getCity())
                .isVerified(providerProfile.is_verified())
                .averageRating(providerProfile.getAvg_rating())
                .totalReviews(providerProfile.getTotal_reviews())
                .createdAt(providerProfile.getCreated_at())
                .build();
    }

    public static UserDTO buildClientDTO(ClientProfile clientProfile) {
        return ClientProfileDTO.builder()
                .user_id(clientProfile.getUser().getId())
                .client_profile_id(clientProfile.getId())
                .fullName(clientProfile.getFullName())
                .city(clientProfile.getCity())
                .createdAt(clientProfile.getCreated())
                .build();
    }
}
