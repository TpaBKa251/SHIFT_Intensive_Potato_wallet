package ru.cft.template.model.response;

import java.time.LocalDate;
import java.util.Date;

public record UserResponse(
        String id,
        String walletId,
        String firstName,
        String lastName,
        String fullName,
        String email,
        Long phone,
        LocalDate registrationDate,
        LocalDate lastUpdateDate,
        LocalDate birthDate
) {
}
