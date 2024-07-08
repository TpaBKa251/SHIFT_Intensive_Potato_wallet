package ru.cft.template.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record WalletResponse(
        UUID id,
        Long amount,
        LocalDateTime lastUpdate
) {
}
