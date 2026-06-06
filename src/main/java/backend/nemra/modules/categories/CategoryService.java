package backend.nemra.modules.categories;

import backend.nemra.modules.categories.dto.CategoryDTO;
import backend.nemra.modules.categories.dto.CreateCategoryRequest;
import backend.nemra.modules.categories.model.Category;
import backend.nemra.shared.response.ApiResponse;
import backend.nemra.shared.utils.MapperToDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories =  categoryRepository.findAll()
                .stream()
                .map(MapperToDTO::toCategoryDTO)
                .toList();

        return ResponseEntity.ok().body(categories);
    }

    @Transactional
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        Category category = new Category();
        category.setNameAr(request.getNameAr());
        category.setNameFr(request.getNameFr());
        category.setNameEn(request.getNameEn());
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Category created successfully", null, true));
    }

    @Transactional
    public ResponseEntity<ApiResponse> updateCategory(@Valid @RequestBody CreateCategoryRequest request, String category_id) {
        Category category = categoryRepository.findById(UUID.fromString(category_id)).orElse(null);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", null, true));
        }
        category.setNameEn(request.getNameEn());
        category.setNameFr(request.getNameFr());
        category.setNameAr(request.getNameAr());
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Category updated successfully", null, true));
    }

    @Transactional
    public ResponseEntity<ApiResponse> deleteCategory(String category_id) {
        Category category = categoryRepository.findById(UUID.fromString(category_id)).orElse(null);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", null, true));
        }
        categoryRepository.delete(category);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("Category deleted successfully", null, true));
    }
}
