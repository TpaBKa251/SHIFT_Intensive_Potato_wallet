package ru.cft.template.model.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InvoiceShortResponse(
        UUID id,
        LocalDateTime dateTime,
        Long amount,
        String comment
) {
}
