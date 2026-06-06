package backend.nemra.modules.users.providers;


import backend.nemra.modules.auth.dto.RegisterProvider;
import backend.nemra.modules.categories.CategoryRepository;
import backend.nemra.modules.categories.model.Category;
import backend.nemra.modules.users.UserRepository;
import backend.nemra.modules.users.model.User;
import backend.nemra.modules.users.providers.dto.ProviderSummaryDTO;
import backend.nemra.modules.users.providers.model.ProviderProfile;
import backend.nemra.shared.response.ApiResponse;
import backend.nemra.shared.utils.MapperToDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProviderService {
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    public ProviderService(
            ProviderRepository providerRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository
    ) {
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<ApiResponse> getProviders() {
        List<ProviderSummaryDTO> providerDTOList =  providerRepository.findAll()
                .stream()
                .map(MapperToDTO::toProviderSummaryDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Providers List", providerDTOList, true));
    }

    public ResponseEntity<ApiResponse> getProvider(UUID providerId) {
        ProviderProfile provider = providerRepository.findByUser_Id(providerId).orElse(null);
        if (provider == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Provider not found", null, false));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Provider", MapperToDTO.buildProviderDTO(provider), true));
    }

    public ResponseEntity<ApiResponse> update(RegisterProvider request) {
        boolean updated = false;
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found", null, false));
        }
        ProviderProfile provider = providerRepository.findByUser_Id(userId).orElse(null);
        if (provider == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Provider not found", null, false));
        }

        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            user.setFullName(request.getFullName());
            updated = true;
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(request.getPhoneNumber());
            updated = true;
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12)));
            updated = true;
        }
        if (request.getCity() != null && !request.getCity().isEmpty()) {
            user.setCity(request.getCity());
            updated = true;
        }
        if (updated) {
            user.setUpdatedAt(LocalDateTime.now());
            user = userRepository.save(user);
            provider.setUser(user);
        }
        if (request.getBusiness_name() != null && !request.getBusiness_name().isEmpty()) {
            provider.setBusinessName(request.getBusiness_name());
        }
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            Category category = categoryRepository.findByNameIgnoreCase(request.getCategory()).orElse(null);
            if  (category == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Category not found", null, false));
            }
            provider.setCategory(category);
        }
        if (request.getBio() != null && !request.getBio().isEmpty()) {
            provider.setBio(request.getBio());
        }
        if (request.getYears_of_experience() != provider.getYearsOfExperience()) {
            provider.setYearsOfExperience(request.getYears_of_experience());
        }
        providerRepository.save(provider);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Provider", MapperToDTO.buildProviderDTO(provider), true));
    }

    public ResponseEntity<ApiResponse> getProviderReviews(UUID providerId) {
        ProviderProfile provider = providerRepository.findByUser_Id(providerId).orElse(null);
        if (provider == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Provider not found", null, false));
        }

    }
}
