package ru.cft.template.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record RegisterBody(
        @Size(min = 5, max = 10, message = "Email must be at least 5 character and no more than 10")
        @NotBlank(message = "User phone number cannot be empty")
        Long phone,

        @Size(min = 8, message = "Email must be at least 8 character")
        @NotBlank(message = "User password cannot be empty")
        String password,

        @Size(min = 1, max = 50, message = "Name must be at least 1 character and no more than 50")
        @NotBlank(message = "User firstname cannot be empty")
        String firstName,

        @Size(min = 1, max = 50, message = "Lastname must be at least 1 character and no more than 50")
        @NotBlank(message = "User Lastname cannot be empty")
        String lastName,

        @Size(max = 50, message = "Middle name must be at least 1 character and no more than 50")
        String middleName,

        @Size(min = 5, max = 100, message = "Email must be at least 5 character and no more than 100")
        @NotBlank(message = "User email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "User age cannot be empty")
        Date birthDate
) {
}
