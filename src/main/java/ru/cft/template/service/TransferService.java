package ru.cft.template.service;

import org.springframework.security.core.Authentication;
import ru.cft.template.entity.Wallet;
import ru.cft.template.model.TransferType;
import ru.cft.template.model.request.AmountBody;
import ru.cft.template.model.request.TransferByIdBody;
import ru.cft.template.model.request.TransferByInvoiceBody;
import ru.cft.template.model.request.TransferByPhoneBody;
import ru.cft.template.model.response.TransferResponse;
import ru.cft.template.model.response.WalletShortResponse;

import java.util.List;
import java.util.UUID;

public interface TransferService {
    TransferResponse createTransferByRecipientPhone(Authentication authentication, TransferByPhoneBody body);

    TransferResponse createTransferByRecipientId(Authentication authentication, TransferByIdBody body);

    TransferResponse createTransferByInvoice(Authentication authentication, TransferByInvoiceBody body);

    WalletShortResponse hesoyam(Authentication authentication, AmountBody body);

    WalletShortResponse casino(Authentication authentication, AmountBody body);

    List<TransferResponse> getAllTransfers(Authentication authentication);

    List<TransferResponse> getAllTransfersByRecipientId(Authentication authentication, UUID recipientId);

    List<TransferResponse> getAllTransfersBySenderWallet(Authentication authentication);

    List<TransferResponse> getAllTransfersByType(Authentication authentication, TransferType type);
}
