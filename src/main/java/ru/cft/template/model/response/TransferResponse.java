package ru.cft.template.model.response;

import lombok.Builder;
import ru.cft.template.model.enums.TransferStatus;
import ru.cft.template.model.enums.TransferType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TransferResponse(
        UUID id,
        UUID senderId,
        UUID recipientId,
        Long recipientPhone,
        UUID invoiceId,
        Long amount,
        LocalDateTime transferDateTime,
        TransferStatus transferStatus,
        TransferType transferType
) {
}
