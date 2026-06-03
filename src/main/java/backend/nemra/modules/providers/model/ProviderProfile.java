package backend.nemra.modules.providers.model;

import backend.nemra.modules.categories.model.Category;
import backend.nemra.modules.users.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "provider_profiles")
@Data
@RequiredArgsConstructor
public class ProviderProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false,
            unique = true
    )
    private User user;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String bio;
    @Column(name = "years_experience")
    private int yearsOfExperience;

    private String city;
    @Column(name = "avatar_url")
    private String avatarUrl;

    private boolean is_verified = false;
    private double avg_rating = 0.;
    private int total_reviews = 0;
    private Date created_at =  new Date();
}
