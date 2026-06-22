package backend.nemra.modules.categories;

import backend.nemra.modules.categories.dto.CategoryDTO;
import backend.nemra.modules.categories.dto.CreateCategoryRequest;
import backend.nemra.modules.categories.dto.UpdateCategory;
import backend.nemra.modules.categories.model.Category;
import backend.nemra.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return categoryService.getCategories();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{category_id}")
    public ResponseEntity<ApiResponse> updateCategory(@Valid @RequestBody CreateCategoryRequest request,  @PathVariable String category_id) {
        return categoryService.updateCategory(request, category_id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{category_id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String category_id) {
        return categoryService.deleteCategory(category_id);
    }
}