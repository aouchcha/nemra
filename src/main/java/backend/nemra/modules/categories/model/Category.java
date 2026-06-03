package backend.nemra.modules.categories.model;

import backend.nemra.modules.providers.model.ProviderProfile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
@RequiredArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "name_fr")
    private String nameFr;
    @Column(name = "name_ar", nullable = false)
    private String nameAr;
    @Column(name = "is_active")
    private boolean isActive = true;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt =  LocalDateTime.now();
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<ProviderProfile> providers;
}
