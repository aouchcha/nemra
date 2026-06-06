package backend.nemra.modules.users.providers;

import backend.nemra.modules.auth.dto.RegisterProvider;
import backend.nemra.modules.users.providers.model.ProviderProfile;
import backend.nemra.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {
    private final ProviderService providerService;
    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getProviders() {
        return providerService.getProviders();
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<ApiResponse> getProvider(@PathVariable UUID user_id) {
        return providerService.getProvider(user_id);
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateProvider(@Valid @RequestBody RegisterProvider request) {
        return providerService.update(request);
    }
}
