package ru.cft.template.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.entity.Transfer;
import ru.cft.template.model.request.TransferByIdBody;
import ru.cft.template.model.request.TransferByInvoiceBody;
import ru.cft.template.model.request.TransferByPhoneBody;
import ru.cft.template.model.response.TransferResponse;
import ru.cft.template.repository.TransferRepository;

import java.util.UUID;

@Component
public class TransferMapper {

    public static Transfer mapTransferByPhoneBodyResponse(TransferByPhoneBody body) {
        Transfer transfer = new Transfer();
        transfer.setRecipientPhone(body.recipientPhone());
        transfer.setAmount(body.amount());

        return transfer;
    }

    public static Transfer mapTransferByIdBodyResponse(TransferByIdBody body) {
        Transfer transfer = new Transfer();
        transfer.setRecipientId(UUID.fromString(body.recipientId()));
        transfer.setAmount(body.amount());

        return transfer;
    }

    public static Transfer mapTransferByInvoiceBodyResponse(TransferByInvoiceBody body) {
        Transfer transfer = new Transfer();
        transfer.setInvoiceNumber(Long.valueOf(body.invoiceId()));

        return transfer;
    }

    public static TransferResponse mapTransferResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getSenderId(),
                transfer.getRecipientId(),
                transfer.getRecipientPhone(),
                transfer.getInvoiceNumber(),
                transfer.getAmount(),
                transfer.getTransferDateTime(),
                transfer.getStatus(),
                transfer.getType()
        );
    }

}
