package ru.cft.template.model.request;


import java.time.LocalDate;
import java.util.Date;

public record UserUpdateBody(
        String firstName,
        String lastName,
        String middleName,
        LocalDate birthDate
) {
}
