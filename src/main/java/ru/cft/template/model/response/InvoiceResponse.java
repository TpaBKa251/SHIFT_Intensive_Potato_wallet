package ru.cft.template.model.response;

import ru.cft.template.model.enums.InvoiceStatus;
import ru.cft.template.model.enums.InvoiceType;

import java.time.LocalDateTime;
import java.util.UUID;

public record InvoiceResponse(
        UUID id,
        UUID senderId,
        UUID recipientId,
        Long amount,
        InvoiceStatus invoiceStatus,
        InvoiceType invoiceType,
        LocalDateTime invoiceDateTime,
        String comment
) {
}
