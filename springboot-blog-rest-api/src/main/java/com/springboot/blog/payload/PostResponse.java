package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "PostResponse Model Information"
)
public class
PostResponse {
    @Schema(
            description = "Blog PostResponse Content "
    )
    private List<PostDto> content;
    @Schema(
            description = "Blog PostResponse PageNo "
    )
    private int pageNo;
    @Schema(
            description = "Blog PostResponse PageSize "
    )
    private int pageSize;
    @Schema(
            description = "Blog PostResponse TotalElement "
    )
    private long totalElements;
    @Schema(
            description = "Blog PostResponse TotalPages "
    )
    private int totalPages;
    @Schema(
            description = "Blog PostResponse Last Page "
    )
    private boolean last;
}
