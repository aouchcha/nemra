package backend.nemra.modules.categories.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class UpdateCategory extends CreateCategoryRequest {
    private String categoryId;
}
