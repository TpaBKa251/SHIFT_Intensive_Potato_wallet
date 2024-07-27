package ru.cft.template.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public record UserUpdateBody(
        @Size(max = 50, message = "First name must be at least 1 character and no more than 50")
        @Pattern(regexp = "^$|^[А-ЯЁ][а-яё]{1,50}$", message = "First name must consist Cyrillic letters and first letter must be uppercase")
        String firstName,

        @Size(max = 50, message = "Last name must be at least 1 character and no more than 50")
        @Pattern(regexp = "^$|^[А-ЯЁ][а-яё]{1,50}$", message = "Last name must consist Cyrillic letters and first letter must be uppercase")
        String lastName,

        @Size(max = 50, message = "Middle name must be no more than 50")
        @Pattern(regexp = "^$|^[А-ЯЁ][а-яё]{1,50}$", message = "Middle name must consist Cyrillic letters and first letter must be uppercase")
        String middleName,

        @Past(message = "You born in future?")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthDate
) {
}
