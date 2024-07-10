package ru.cft.template.model.request;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public record RegisterBody(
        //@Size(min = 5, max = 10, message = "Email must be at least 5 character and no more than 10")
        @NotNull(message = "User phone number cannot be empty")
        @Min(value = 79000000000L)
        @Max(value = 79999999999L)
        Long phone,

        @Size(min = 8, message = "Password must be at least 8 character")
        @NotBlank(message = "User password cannot be empty")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()!*])(?=\\S+$).{8,20}$", message = "Password must contain at least one of each type of characters: lowercase and uppercase letters, a number, a special character")
        String password,

        @Size(min = 1, max = 50, message = "Name must be at least 1 character and no more than 50")
        @NotBlank(message = "User firstname cannot be empty")
        String firstName,

        @Size(min = 1, max = 50, message = "Lastname must be at least 1 character and no more than 50")
        @NotBlank(message = "User Lastname cannot be empty")
        String lastName,

        @Size(max = 50, message = "Middle name must no more than 50 or null")
        String middleName,

        @Size(min = 5, max = 100, message = "Email must be at least 5 character and no more than 100")
        @NotBlank(message = "User email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @NotNull(message = "User birthdate cannot be empty")
        LocalDate birthDate
) {
}
