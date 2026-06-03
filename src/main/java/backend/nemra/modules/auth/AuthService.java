package backend.nemra.modules.auth;

import backend.nemra.modules.auth.dto.AuthResponse;
import backend.nemra.modules.auth.dto.RegisterClient;
import backend.nemra.modules.auth.dto.RegisterProvider;
import backend.nemra.modules.categories.CategoryRepository;
import backend.nemra.modules.categories.model.Category;
import backend.nemra.modules.clients.ClientRepository;
import backend.nemra.modules.clients.model.ClientProfile;
import backend.nemra.modules.providers.ProviderRepository;
import backend.nemra.modules.providers.dto.ProviderDTO;
import backend.nemra.modules.providers.model.ProviderProfile;
import backend.nemra.modules.users.UserRepository;
import backend.nemra.modules.users.dto.ClientProfileDTO;
import backend.nemra.modules.users.dto.UserDTO;
import backend.nemra.modules.users.model.Role;
import backend.nemra.modules.users.model.User;
import backend.nemra.shared.response.ApiResponse;
import backend.nemra.shared.utils.jwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final jwtUtils jwtUtils;

    public AuthService(
            UserRepository userRepository ,
            ClientRepository clientRepository,
            ProviderRepository providerRepository,
            CategoryRepository categoryRepository,
            jwtUtils jwtUtils
    ) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.providerRepository = providerRepository;
        this.categoryRepository = categoryRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public ResponseEntity<ApiResponse> RegisterClient(RegisterClient request) {
        System.out.println("RegisterClient targeted");
        try {
            User newUser = new User();
            newUser.setFullName(request.getFullName());
            newUser.setPhoneNumber(request.getPhoneNumber());
            newUser.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12)));
            newUser.setRole(Role.CLIENT);
            newUser = userRepository.save(newUser);

            ClientProfile newClientProfile = new ClientProfile();
            newClientProfile.setUser(newUser);
            newClientProfile.setFullName(request.getFullName());
            newClientProfile.setCity(request.getCity());
            newClientProfile.setCreated(new Date());
            clientRepository.save(newClientProfile);

            String token = jwtUtils.generateToken(newUser.getId(), Role.CLIENT.toString());

            ClientProfileDTO newClientProfileDTO = ClientProfileDTO.builder()
                    .user_id(newUser.getId())
                    .client_profile_id(newClientProfile.getId())
                    .fullName(request.getFullName())
                    .city(request.getCity())
                    .createdAt(newUser.getCreatedAt())
                    .build();

            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setUser(newClientProfileDTO);


            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Client created successfully", authResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Server error", e.getMessage()));
        }

    }

    @Transactional
    public ResponseEntity<ApiResponse> RegisterProvider(RegisterProvider request) {
        Category category = categoryRepository.findByNameIgnoreCase(request.getCategory()).orElse(null);
        if  (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Category not found", null));
        }
        User newUser = new User();
        newUser.setFullName(request.getFullName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12)));
        newUser.setRole(Role.PROVIDER);
        newUser = userRepository.save(newUser);

        ProviderProfile providerProfile = new ProviderProfile();
        providerProfile.setUser(newUser);
        providerProfile.setBusinessName(request.getBusiness_name());
        providerProfile.setCategory(category);
        providerProfile.setBio(request.getBio());
        providerProfile.setYearsOfExperience(request.getYears_of_experience());
        providerProfile.setCity(request.getCity());
        providerRepository.save(providerProfile);

        String  token = jwtUtils.generateToken(newUser.getId(), Role.PROVIDER.toString());

        ProviderDTO dto = ProviderDTO.builder()
                .providerId(providerProfile.getId())
                .user_id(newUser.getId())
                .businessName(request.getBusiness_name())
                .category(category.getNameAr())
                .bio(request.getBio())
                .yearsOfExperience(request.getYears_of_experience())
                .city(request.getCity())
                .isVerified(providerProfile.is_verified())
                .averageRating(providerProfile.getAvg_rating())
                .totalReviews(providerProfile.getTotal_reviews())
                .createdAt(providerProfile.getCreated_at())
                .build();

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUser(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Provider created successfully", authResponse));
    }
}