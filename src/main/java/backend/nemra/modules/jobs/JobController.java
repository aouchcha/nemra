package backend.nemra.modules.jobs;

import backend.nemra.modules.jobs.dto.CreateJobRequest;
import backend.nemra.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ApiResponse> createJob(@Valid @RequestBody CreateJobRequest createJobRequest) {
        return jobService.createJob(createJobRequest);
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> getMyJobs() {
        return jobService.getMyJobs();
    }

    @GetMapping("/{job_id}")
    public ResponseEntity<ApiResponse> getJob(@PathVariable UUID job_id) {
        return jobService.getJob(job_id);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PatchMapping("/{job_id}/accept")
    public ResponseEntity<ApiResponse> acceptJob(@PathVariable UUID job_id) {
        return jobService.accept(job_id);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PatchMapping("/{job_id}/complete")
    public ResponseEntity<ApiResponse> completeJob(@PathVariable UUID job_id) {
        return jobService.complete(job_id);
    }

    @PatchMapping("/{job_id}/cancel")
    public ResponseEntity<ApiResponse> cancelJob(@PathVariable UUID job_id) {
        return jobService.cancel(job_id);
    }
}
