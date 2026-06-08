package backend.nemra.modules.jobs;

import backend.nemra.modules.jobs.dto.CreateJobRequest;
import backend.nemra.modules.jobs.dto.JobDTO;
import backend.nemra.modules.jobs.model.Job;
import backend.nemra.modules.jobs.model.JobStatus;
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
public class JobService {
    private final  JobRepository jobRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    public JobService(
            JobRepository jobRepository,
            ClientRepository clientRepository,
            ProviderRepository providerRepository,
            UserRepository userRepository
    ) {
        this.jobRepository = jobRepository;
        this.clientRepository = clientRepository;
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ApiResponse> createJob(CreateJobRequest request) {
        UUID user_id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        final User user = userRepository.findById(user_id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("client not found", null, false));
        }

        if (!user.getRole().equals(Role.CLIENT)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("You aare not a client", null, false));
        }

        Job job = new Job();
        job.setClient(user.getClientProfile());
        job.setDescription(request.getDescription());
        job =  jobRepository.save(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("job created", MapperToDTO.buildJobDTO(job), true));
    }

    public ResponseEntity<ApiResponse> getMyJobs() {
        UUID user_id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        final User user = userRepository.findById(user_id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("user not found", null, false));
        }
        List<Job> myJobs;
        if (user.getRole().equals(Role.CLIENT)) {
            ClientProfile client = clientRepository.findById(user.getId()).orElse(null);
            if (client == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("client not found", null, false));
            }
            myJobs = client.getJobsAsClient();
        } else if (user.getRole().equals(Role.PROVIDER)) {
            ProviderProfile provider = providerRepository.findById(user.getId()).orElse(null);
            if (provider == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("provider not found", null, false));
            }
            myJobs = provider.getJobsAsProvider();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("invalid role", null, false));
        }
        List<JobDTO> data = myJobs.stream()
                .map(MapperToDTO::buildJobDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("My Jobs", data, true));
    }

    public ResponseEntity<ApiResponse> getJob(UUID id) {
        final Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("job not found", null, false));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("job", MapperToDTO.buildJobDTO(job), true));
    }

    public ResponseEntity<ApiResponse> accept(UUID id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("job not found", null, false));
        }
        UUID user_id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        final User user = userRepository.findById(user_id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("user not found", null, false));
        }
        final ProviderProfile provider = providerRepository.findByUser_Id(user.getId()).orElse(null);
        if (provider == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("provider not found", null, false));
        }
        job.setProvider(provider);
        job.setStatus(JobStatus.ACCEPTED);
        job = jobRepository.save(job);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("job", MapperToDTO.buildJobDTO(job), true));
    }

    public ResponseEntity<ApiResponse> complete(UUID id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("job not found", null, false));
        }
        UUID user_id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        final User user = userRepository.findById(user_id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("user not found", null, false));
        }
        if (!jobRepository.existsByProviderId(user_id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("user not part of this job", null, false));
        }
        job.setStatus(JobStatus.COMPLETED);
        job = jobRepository.save(job);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("job", MapperToDTO.buildJobDTO(job), true));
    }

    public ResponseEntity<ApiResponse> cancel(UUID id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("job not found", null, false));
        }
        UUID user_id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        final User user = userRepository.findById(user_id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("user not found", null, false));
        }
        if (!jobRepository.existsByProviderIdOrClientId(user_id, user_id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("user not part of this job", null, false));
        }
        job.setStatus(JobStatus.CANCELED);
        job = jobRepository.save(job);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("job", MapperToDTO.buildJobDTO(job), true));
    }
}
