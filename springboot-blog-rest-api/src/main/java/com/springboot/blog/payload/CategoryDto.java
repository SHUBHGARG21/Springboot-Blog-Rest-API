package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "CategoryDTO Model Information"
)
public class CategoryDto {

    private long id;
    @Schema(
            description = "Blog Category Name"
    )
    private String name;
    @Schema(
            description = "Blog Category Description"
    )
    private String description;
}
