package ru.cft.template.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;

public record UserUpdateBody(
        @Size(min = 1, max = 50, message = "Name must be at least 1 character and no more than 50")
        String firstName,

        @Size(min = 1, max = 50, message = "Lastname must be at least 1 character and no more than 50")
        String lastName,

        @Size(max = 50, message = "Middle name must be at least 1 character and no more than 50")
        String middleName,

        LocalDate birthDate
) {
}
