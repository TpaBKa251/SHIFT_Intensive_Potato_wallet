package ru.cft.template.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.entity.Transfer;
import ru.cft.template.model.request.TransferByIdBody;
import ru.cft.template.model.request.TransferByInvoiceBody;
import ru.cft.template.model.request.TransferByPhoneBody;
import ru.cft.template.model.response.TransactionHistoryResponse;
import ru.cft.template.model.response.TransferResponse;
import ru.cft.template.repository.TransferRepository;

import java.util.UUID;

@Component
public class TransferMapper {

    public static TransactionHistoryResponse mapTransactionToHistory(Transfer transaction) {
        return new TransactionHistoryResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getTransferDateTime(),
                transaction.getType(),
                transaction.getRecipientPhone(),
                transaction.getStatus()
        );
    }

    public static TransferResponse mapTransferResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getSenderId(),
                transfer.getRecipientId(),
                transfer.getRecipientPhone(),
                transfer.getInvoiceId(),
                transfer.getAmount(),
                transfer.getTransferDateTime(),
                transfer.getStatus(),
                transfer.getType()
        );
    }

}
