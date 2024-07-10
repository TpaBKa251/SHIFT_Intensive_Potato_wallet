package ru.cft.template.model.response;

import org.hibernate.resource.transaction.spi.TransactionStatus;
import ru.cft.template.model.enums.TransferStatus;
import ru.cft.template.model.enums.TransferType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record TransactionHistoryResponse(
        UUID id,
        Long amount,
        LocalDateTime transactionDate,
        TransferType type,
        Long receiverPhone,
        TransferStatus status
) {
}
