package ru.cft.template.model.response;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.cft.template.model.TransferStatus;
import ru.cft.template.model.TransferType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TransferResponse(
        UUID id,
        UUID senderId,
        UUID recipientId,
        Long recipientPhone,
        Long invoiceNumber,
        Long amount,
        LocalDateTime transferDateTime,
        TransferStatus transferStatus,
        TransferType transferType
) {
}
