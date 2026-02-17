package org.example.demo2.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Long id;

    @NotBlank(message = "First name must not be empty")
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    private String lastName;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
            message = "Email must end with @gmail.com"
    )
    private String email;

    @NotBlank(message = "Username must not be empty")
    private String username;
    private Long createdBy;
    private Long modifiedBy;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()_-]).{8,}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
    private String password;

    @NotBlank(message = "Role must not be empty")
    private String role;
}
