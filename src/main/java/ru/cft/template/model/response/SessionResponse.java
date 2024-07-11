package ru.cft.template.model.response;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record SessionResponse(
        UUID id,
        UUID userId,
        String token,
        Date expirationTime
) {
}
