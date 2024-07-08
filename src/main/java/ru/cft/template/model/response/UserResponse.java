package ru.cft.template.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public record UserResponse(
        String id,
        String walletId,
        String firstName,
        String lastName,
        String fullName,
        String email,
        Long phone,
        LocalDateTime registrationDate,
        LocalDateTime lastUpdateDate,
        LocalDate birthDate
) {
}
