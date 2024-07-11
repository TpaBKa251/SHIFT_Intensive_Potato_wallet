package ru.cft.template.model.response;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record CurrentSessionResponse(
        UUID id,
        UUID userId,
        Date expirationTime,
        Boolean active
) {
}
