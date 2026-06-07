package backend.nemra.modules.reviews.model;

import backend.nemra.modules.jobs.model.Job;
import backend.nemra.modules.users.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false,  unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "job_id",
            referencedColumnName = "id",
            nullable = false,
            unique = true
    )
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reviewer_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reviewed_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User reviewed;

    @Enumerated(EnumType.STRING)
    @Column(name = "reviewer_type")
    private ReviewerType reviewerType;
    
    @Column(name = "rating_overall")
    private int ratingOverall = 1;

    //Filled When The reviewer in the client

    @Column(name = "rating_quality")
    private int ratingQuality = 1;

    @Column(name = "rating_punctuality")
    private int ratingPunctuality = 1;

    @Column(name = "rating_communication")
    private int ratingCommunication = 1;

    @Column(name = "rating_price_fairness")
    private int ratingPriceFairness = 1;

    //Filled when the reviewer is the provider
    @Column(name = "rating_payment")
    private int ratingPayment = 1;

    @Column(name = "rating_respect")
    private int ratingRespect = 1;

    private String comment;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt =  LocalDateTime.now();
}
