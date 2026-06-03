package backend.nemra.modules.categories;


import backend.nemra.modules.categories.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByNameAr(String nameAr);
    Optional<Category> findByNameEn(String nameEn);
    Optional<Category> findByNameFr(String nameFr);

    @Query("SELECT c FROM Category c WHERE " +
            "LOWER(c.nameAr) = LOWER(:name) OR " +
            "LOWER(c.nameEn) = LOWER(:name) OR " +
            "LOWER(c.nameFr) = LOWER(:name)")
    Optional<Category> findByNameIgnoreCase(@Param("name") String name);
}
