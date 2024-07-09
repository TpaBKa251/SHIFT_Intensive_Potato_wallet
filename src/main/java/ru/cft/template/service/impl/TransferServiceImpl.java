package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.Transfer;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.exception.BadTransactionException;
import ru.cft.template.mapper.TransferMapper;
import ru.cft.template.model.TransferStatus;
import ru.cft.template.model.TransferType;
import ru.cft.template.model.request.AmountBody;
import ru.cft.template.model.request.TransferByIdBody;
import ru.cft.template.model.request.TransferByInvoiceBody;
import ru.cft.template.model.request.TransferByPhoneBody;
import ru.cft.template.model.response.TransferResponse;
import ru.cft.template.model.response.WalletShortResponse;
import ru.cft.template.repository.TransferRepository;
import ru.cft.template.repository.WalletRepository;
import ru.cft.template.service.TransferService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final UserServiceImpl userService;
    private final WalletRepository walletRepository;


    @Override
    public TransferResponse createTransferByRecipientPhone(Authentication authentication, TransferByPhoneBody body) {
        User user = userService.getUserByAuthentication(authentication);
        Wallet senderWallet = user.getWallet();

        Transfer transfer = new Transfer();

        transfer.setAmount(body.amount());
        transfer.setTransferDateTime(LocalDateTime.now());
        transfer.setStatus(TransferStatus.SUCCESSFUL);
        transfer.setSenderWallet(senderWallet);

        if (body.recipientPhone() != null) {
            createTransferByPhone(transfer, body, senderWallet);
        } else {
            throw new BadTransactionException("Invalid transfer request");
        }

        senderWallet.setAmount(senderWallet.getAmount() - body.amount());
        walletRepository.save(senderWallet);

        transfer.setStatus(TransferStatus.SUCCESSFUL);
        transferRepository.save(transfer);

        return TransferMapper.mapTransferResponse(transfer);
    }

    private void createTransferByPhone(
            Transfer transaction,
            TransferByPhoneBody body,
            Wallet senderWallet
    ) {
        transaction.setType(TransferType.TRANSFER);
        Wallet receiverWallet = userService.findUserByPhone(body.recipientPhone()).getWallet();

        if (receiverWallet == null) {
            throw new BadTransactionException("Receiver wallet not found");
        }

        GetValidTransaction(body, senderWallet, transaction);

        receiverWallet.setAmount(receiverWallet.getAmount() + body.amount());
        walletRepository.save(receiverWallet);
    }

    private void GetValidTransaction(TransferByPhoneBody body, Wallet senderWallet, Transfer transfer) {
        if (!isValidTransfer(body)){
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new BadTransactionException("Invalid transaction request");
        }

        if (!isValidWallet(body, senderWallet)){
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new BadTransactionException("Insufficient funds or wallet not found");
        }
    }

    private boolean isValidWallet(TransferByPhoneBody body, Wallet senderWallet){
        return senderWallet != null && senderWallet.getAmount() >= body.amount();
    }

    private boolean isValidTransfer(TransferByPhoneBody body) {
        if (body.amount() == null || body.amount() <= 0) {
            return false;
        }

        return (body.recipientPhone() != null);
    }

    @Override
    public TransferResponse createTransferByRecipientId(Authentication authentication, TransferByIdBody body) {
        return null;
    }

    @Override
    public TransferResponse createTransferByInvoice(Authentication authentication, TransferByInvoiceBody body) {
        return null;
    }

    @Override
    public WalletShortResponse hesoyam(Authentication authentication, AmountBody body) {

        if (body.amount() == null) {
            throw new BadTransactionException("You need to specify the amount to hesoyam from 1 to 1000 m.u.");
        }

        if (body.amount() > 1000) {
            throw new BadTransactionException("Too much amount to hesoyam, we poor company and don't have enough money. " +
                    "Please specify the amount no more than 1000 m.u.");
        }

        User user = userService.getUserByAuthentication(authentication);
        Wallet senderWallet = user.getWallet();

        List<Transfer> lastRefillTransactions = transferRepository
                .findLastTransactionsByTypeAndWalletId(
                        TransferType.REPLENISHMENT,
                        senderWallet.getId(),
                        PageRequest.of(0, 4)
                );

        Transfer refillTransaction = new Transfer();
        refillTransaction.setSenderWallet(senderWallet);
        refillTransaction.setType(TransferType.REPLENISHMENT);
        refillTransaction.setTransferDateTime(LocalDateTime.now());
        refillTransaction.setStatus(replenishment(lastRefillTransactions)
                ? TransferStatus.SUCCESSFUL : TransferStatus.FAILED);

        if (TransferStatus.SUCCESSFUL.equals(refillTransaction.getStatus())) {
            senderWallet.setAmount(senderWallet.getAmount() + body.amount());
            refillTransaction.setAmount(body.amount());
            walletRepository.save(senderWallet);
        } else {
            refillTransaction.setAmount(body.amount());
        }

        transferRepository.save(refillTransaction);

        return new WalletShortResponse(
                senderWallet.getId(),
                senderWallet.getAmount()
        );
    }

    @Override
    public List<TransferResponse> getAllTransfers(Authentication authentication) {
        return List.of();
    }

    private boolean replenishment(List<Transfer> transfers) {
        Random random = new Random();
        if (transfers.size() >= 3 && transfers.stream()
                .filter(t -> TransferStatus.FAILED.equals(t.getStatus())).count() >= 3) {
            return true;
        }
        return random.nextInt(4) == 0;
    }
}
