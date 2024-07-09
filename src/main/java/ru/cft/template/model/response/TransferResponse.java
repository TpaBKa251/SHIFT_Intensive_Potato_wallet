package ru.cft.template.model.response;

import ru.cft.template.model.TransferStatus;
import ru.cft.template.model.TransferType;

import java.time.LocalDateTime;

public record TransferResponse(
        String id,
        String senderId,
        String recipientId,
        Long amount,
        LocalDateTime transferDateTime,
        TransferStatus transferStatus,
        TransferType transferType
) {
}
