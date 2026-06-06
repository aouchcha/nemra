package backend.nemra.modules.auth;

import backend.nemra.modules.auth.dto.AuthResponse;
import backend.nemra.modules.auth.dto.LoginRequest;
import backend.nemra.modules.auth.dto.RegisterClient;
import backend.nemra.modules.auth.dto.RegisterProvider;
import backend.nemra.modules.auth.model.RefreshToken;
import backend.nemra.modules.categories.CategoryRepository;
import backend.nemra.modules.categories.model.Category;
import backend.nemra.modules.users.clients.ClientRepository;
import backend.nemra.modules.users.clients.model.ClientProfile;
import backend.nemra.modules.users.providers.ProviderRepository;
import backend.nemra.modules.users.providers.model.ProviderProfile;
import backend.nemra.modules.users.UserRepository;
import backend.nemra.modules.users.dto.UserDTO;
import backend.nemra.modules.users.model.Role;
import backend.nemra.modules.users.model.User;
import backend.nemra.shared.response.ApiResponse;
import backend.nemra.shared.utils.MapperToDTO;
import backend.nemra.shared.utils.jwtUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final jwtUtils jwtUtils;

    public AuthService(
            AuthRepository authRepository,
            UserRepository userRepository ,
            ClientRepository clientRepository,
            ProviderRepository providerRepository,
            CategoryRepository categoryRepository,
            jwtUtils jwtUtils
    ) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.providerRepository = providerRepository;
        this.categoryRepository = categoryRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public ResponseEntity<ApiResponse> registerClient(RegisterClient request) {
        System.out.println("RegisterClient targeted");
        try {
            User newUser = new User();
            newUser.setFullName(request.getFullName());
            newUser.setPhoneNumber(request.getPhoneNumber());
            newUser.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12)));
            newUser.setRole(Role.CLIENT);
            newUser.setCity(request.getCity());
            newUser = userRepository.save(newUser);

            ClientProfile newClientProfile = new ClientProfile();
            newClientProfile.setUser(newUser);
            newClientProfile.setFullName(request.getFullName());
            newClientProfile.setCity(request.getCity());
            newClientProfile = clientRepository.save(newClientProfile);

            String accessToken = jwtUtils.generateAccessToken(newUser.getId(), Role.CLIENT.toString());
            String refreshToken = jwtUtils.generateRefreshToken(newUser.getId());
            RefreshToken t = new RefreshToken();
            t.setUser(newUser);
            t.setRefreshToken(refreshToken);
            authRepository.save(t);

            UserDTO newClientProfileDTO = MapperToDTO.buildClientDTO(newClientProfile);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(accessToken);
            authResponse.setRefreshToken(refreshToken);
            authResponse.setUser(newClientProfileDTO);


            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Client created successfully", authResponse, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Server error", e.getMessage(), false));
        }

    }

    @Transactional
    public ResponseEntity<ApiResponse> registerProvider(RegisterProvider request) {
        Category category = categoryRepository.findByNameIgnoreCase(request.getCategory()).orElse(null);
        if  (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Category not found", null, false));
        }
        User newUser = new User();
        newUser.setFullName(request.getFullName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12)));
        newUser.setRole(Role.PROVIDER);
        newUser.setCity(request.getCity());
        newUser = userRepository.save(newUser);

        ProviderProfile providerProfile = new ProviderProfile();
        providerProfile.setUser(newUser);
        providerProfile.setBusinessName(request.getBusiness_name());
        providerProfile.setCategory(category);
        providerProfile.setBio(request.getBio());
        providerProfile.setYearsOfExperience(request.getYears_of_experience());
        providerProfile.setCity(request.getCity());
        providerProfile = providerRepository.save(providerProfile);

        String accessToken = jwtUtils.generateAccessToken(newUser.getId(), Role.PROVIDER.toString());
        String refreshToken = jwtUtils.generateRefreshToken(newUser.getId());
        RefreshToken t = new RefreshToken();
        t.setUser(newUser);
        t.setRefreshToken(refreshToken);
        authRepository.save(t);

        UserDTO dto = MapperToDTO.buildProviderDTO(providerProfile);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Provider created successfully", authResponse, true));
    }

    public ResponseEntity<ApiResponse> login(LoginRequest loginRequest) {
        final User user = userRepository.findByPhoneNumber(loginRequest.getNumber()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Bad Credentials", null, false));
        }
        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Bad Credentials", null, false));
        }
        RefreshToken token = authRepository.findByUser_Id(user.getId()).orElse(null);
        String refreshToken;
        if (token == null || !jwtUtils.validateToken(token.getRefreshToken())) {
            refreshToken = jwtUtils.generateRefreshToken(user.getId());
            RefreshToken t = new RefreshToken();
            t.setUser(user);
            t.setRefreshToken(refreshToken);
            authRepository.save(t);
        } else {
            refreshToken = token.getRefreshToken();
        }
        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getRole().toString());

        UserDTO dto;
        if (user.getRole().equals(Role.CLIENT)) {
            final ClientProfile clientProfile = clientRepository.findByUser_Id(user.getId()).orElse(null);
            if (clientProfile == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Client not found", null, false));
            }
            dto = MapperToDTO.buildClientDTO(clientProfile);

        }else if (user.getRole().equals(Role.PROVIDER)) {
            final ProviderProfile providerProfile = providerRepository.findByUser_Id(user.getId()).orElse(null);
            if (providerProfile == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Client not found", null, false));
            }
            dto = MapperToDTO.buildProviderDTO(providerProfile);
        } else if (user.getRole().equals(Role.ADMIN)) {
            dto = MapperToDTO.buildUserDTO(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User Not Found", null, false));
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setUser(dto);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User login successfully", authResponse, true));
    }

    public ResponseEntity<ApiResponse> refresh(String refreshToken) {
        if (!authRepository.existsByRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Refresh token not found", null, false));
        }

        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        RefreshToken dbToken = authRepository.findByRefreshTokenAndUser_Id(refreshToken, userId).orElse(null);

        if (dbToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid token", null, false));
        }

        if (!jwtUtils.validateToken(dbToken.getRefreshToken())) {
            authRepository.delete(dbToken);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid token", null, false));
        }

        String accessToken = jwtUtils.generateAccessToken(dbToken.getUser().getId(), dbToken.getUser().getRole().toString());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(dbToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Refreshed successfully", authResponse, true));
    }

    public ResponseEntity<ApiResponse> logout(String refreshToken) {
        System.out.println("Refresh token: " + refreshToken);
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("User id: " + userId);
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("User not found", null, false));
        }
        RefreshToken DbRefreshToken = authRepository.findByRefreshTokenAndUser_Id(refreshToken,userId).orElse(null);
        if (DbRefreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Refresh token not found", null, false));
        }
        authRepository.delete(DbRefreshToken);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("Logout successfully", null, true));
    }
}