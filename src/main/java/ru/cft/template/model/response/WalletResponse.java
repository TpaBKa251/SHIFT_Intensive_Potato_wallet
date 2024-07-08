package ru.cft.template.model.response;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record WalletResponse(
        UUID id,
        Long amount,
        LocalDate lastUpdate
) {
}
