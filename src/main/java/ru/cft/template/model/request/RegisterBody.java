package ru.cft.template.model.request;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public record RegisterBody(
        //@Size(min = 5, max = 10, message = "Email must be at least 5 character and no more than 10")
        @NotNull(message = "User phone number cannot be empty")
        @Min(value = 70000000000L, message = "Phone must start with 7 and hold 11 digits")
        @Max(value = 79999999999L,  message = "Phone must start with 7 and hold 11 digits")
        Long phone,

        @Size(min = 8, max = 64, message = "Password must be at least 8 character and no more than 64")
        @NotBlank(message = "Password cannot be empty")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()!*])(?=\\S+$).{8,64}$", message = "Password must contain at least one of each type of characters:" +
                " lowercase and uppercase letters, a number, a special character")
        String password,

        @Size(min = 1, max = 50, message = "First name must be at least 1 character and no more than 50")
        @NotBlank(message = "First name cannot be empty")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "First name must consist Cyrillic letters and first letter must be uppercase")
        String firstName,

        @Size(min = 1, max = 50, message = "Last name must be at least 1 character and no more than 50")
        @NotBlank(message = "Last name cannot be empty")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "Last name must consist Cyrillic letters and first letter must be uppercase")
        String lastName,

        @Size(max = 50, message = "Middle name must be no more than 50")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "Middle name must consist Cyrillic letters and first letter must be uppercase")
        String middleName,

        @Size(min = 5, max = 100, message = "Email must be at least 5 character and no more than 100")
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @NotNull(message = "Birthdate cannot be empty")
        @Past(message = "You born in future?")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthDate
) {
}
