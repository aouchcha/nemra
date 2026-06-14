package backend.nemra.modules.calls;

import backend.nemra.modules.calls.dto.CallRequest;
import backend.nemra.modules.calls.dto.CallResponse;
import backend.nemra.modules.calls.model.Call;
import backend.nemra.modules.calls.model.CallStatus;
import backend.nemra.modules.users.UserRepository;
import backend.nemra.modules.users.clients.ClientRepository;
import backend.nemra.modules.users.clients.model.ClientProfile;
import backend.nemra.modules.users.model.Role;
import backend.nemra.modules.users.model.User;
import backend.nemra.modules.users.providers.ProviderRepository;
import backend.nemra.modules.users.providers.model.ProviderProfile;
import backend.nemra.shared.response.ApiResponse;
import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.WebhookReceiver;
import livekit.LivekitWebhook.WebhookEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class CallService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository  providerRepository;
    private final String apiKey;
    private final String apiSecret;
    private final String liveKitUrl;
    private final ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();
    private final CallRepository callRepository;

    public CallService(
            UserRepository userRepository,
            ClientRepository clientRepository,
            ProviderRepository providerRepository,
            @Value("${livekit.api-key}") String apiKey,
            @Value("${livekit.api-secret}") String apiSecret,
            @Value("${livekit.url}") String liveKitUrl,
            ScheduledExecutorService scheduler,
            CallRepository callRepository
    ){
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.providerRepository = providerRepository;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.liveKitUrl = liveKitUrl;
        this.scheduler = scheduler;
        this.callRepository = callRepository;
    }

    public ResponseEntity<ApiResponse> generateToken(CallRequest callRequest) {
        UUID user_id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        final User user = userRepository.findById(user_id).orElse(null);
        if  (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("invalid User", null, false));
        }
        final ClientProfile client =  clientRepository.findById(callRequest.getClientId()).orElse(null);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("client id invalid", null, false));
        }
        final ProviderProfile provider = providerRepository.findById(callRequest.getProviderId()).orElse(null);
        if (provider == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("provider id invalid", null, false));
        }
        final String roomName = String.join("-", "call",client.getUser().getId().toString(),provider.getUser().getId().toString());
        System.out.println("Room Name: " + roomName);
        AccessToken accessToken = new AccessToken(apiKey, apiSecret);
        if (user.getRole().equals(Role.CLIENT)) {
            accessToken.setName(client.getFullName());
            accessToken.setIdentity(client.getUser().getId().toString());
            Call call = new Call();
            call.setRoomName(roomName);
            callRepository.save(call);
        }else if  (user.getRole().equals(Role.PROVIDER)) {
            accessToken.setName(provider.getUser().getFullName());
            accessToken.setIdentity(provider.getUser().getId().toString());
        }
        accessToken.addGrants(new RoomJoin(true), new RoomName(roomName));
        accessToken.setTtl(2 * 60 * 60 * 1000); // 2 hours in milliseconds
        final String token = accessToken.toJwt();
        CallResponse callResponse = new CallResponse(token, liveKitUrl);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("token generated", callResponse, true));
    }

    public ResponseEntity<?> webhookReceive(String rawBody, String authHeader) {
        WebhookReceiver receiver =  new WebhookReceiver(apiKey, apiSecret);
        WebhookEvent event = receiver.receive(rawBody, authHeader);
        switch (event.getEvent()) {
            case "room_finished":
                updateCallStatus(event.getRoom().getName(), null, CallStatus.FINISHED);
                break;
            case "participant_joined":
                updateCallStatus(event.getRoom().getName(), event.getParticipant().getIdentity(), null);
                break;
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private boolean checkIfTheProviderJoined(String roomName) {
        final Call call = callRepository.findFirstByRoomNameOrderByCreatedAtDesc(roomName).orElse(null);
        if (call == null || call.getStatus().equals(CallStatus.RINGING)) {
            return false;
        }
        return true;
    }
    @Transactional
    protected void updateCallStatus(String roomName, String userId, CallStatus status) {
        Call call = callRepository.findFirstByRoomNameOrderByCreatedAtDesc(roomName).orElse(null);
        if (call == null) {
            return;
        }
        if (userId != null) {
            final User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
            if (user == null) {
                return;
            }

            if (user.getRole().equals(Role.CLIENT)) {
                // client joined → start the 30s timer waiting for provider
                call.setStatus(CallStatus.RINGING);
                call.setCaller(user.getClientProfile());
                ScheduledFuture<?> future = scheduler.schedule(() -> {
                    if (!checkIfTheProviderJoined(roomName)) {
                        System.out.println("FUTURE IS RUNNING");
                        Call c = callRepository.findFirstByRoomNameOrderByCreatedAtDesc(roomName).orElse(null);
                        if (c != null) {
                            c.setStatus(CallStatus.REFUSED);
                            callRepository.save(c);
                        }
                    }
                }, 30, TimeUnit.SECONDS);
                futures.put(roomName, future);
            } else if (user.getRole().equals(Role.PROVIDER)) {
                // provider joined → cancel the timer, call is live
                call.setStatus(CallStatus.ACCEPTED);
                call.setCalled(user.getProviderProfile());
                ScheduledFuture<?> future = futures.remove(roomName);
                if (future != null) future.cancel(false);
            }

        }else {
            call.setStatus(status);
            call.setFinishedAt(LocalDateTime.now());
        }
        callRepository.save(call);
    }
}
