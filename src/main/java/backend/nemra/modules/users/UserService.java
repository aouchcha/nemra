package backend.nemra.modules.users;

import backend.nemra.modules.auth.dto.RegisterRequest;
import backend.nemra.modules.users.clients.ClientRepository;
import backend.nemra.modules.users.clients.model.ClientProfile;
import backend.nemra.modules.users.model.User;
import backend.nemra.shared.response.ApiResponse;
import backend.nemra.shared.utils.MapperToDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public UserService(
            UserRepository userRepository,
            ClientRepository clientRepository
    ) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    public ResponseEntity<ApiResponse> getMe() {
        UUID user_id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(user_id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found", null, false));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user", MapperToDTO.buildUserDTO(user), true));
    }

    @Transactional
    public ResponseEntity<ApiResponse> updateMe(RegisterRequest request) {
        UUID user_id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(user_id).orElse(null);
        ClientProfile client = clientRepository.findByUser_Id(user_id).orElse(null);
        boolean updated = false;
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found", null, false));
        }
        if  (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Client not found", null, false));
        }
        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            user.setFullName(request.getFullName());
            client.setFullName(request.getFullName());
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
            client.setCity(request.getCity());
            updated = true;
        }
        if (updated) {
            user.setUpdatedAt(LocalDateTime.now());
            user = userRepository.save(user);
            client.setUser(user);
        }
        clientRepository.save(client);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user", MapperToDTO.buildUserDTO(user), true));
    }
}