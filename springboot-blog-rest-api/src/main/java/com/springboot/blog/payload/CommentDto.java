package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
@Schema(
        description = "CommentDTO Model Information"
)
public class CommentDto {
    private long id;
    // name should not be null or empty
    @Schema(
            description = "Blog Comment Name"
    )
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    // email should not be null or empty
    // email field validation
    @Schema(
            description = "Blog Comment Email"
    )
    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    // comment body should not be bull or empty
    // Comment body must be minimum 10 characters
    @Schema(
            description = "Blog Comment body"
    )
    @NotEmpty(message = "Comment body must not be empty")
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    private String body;
}
