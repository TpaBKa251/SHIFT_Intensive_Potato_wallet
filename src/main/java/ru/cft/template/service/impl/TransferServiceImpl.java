package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.Transfer;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.exception.AccessRightsException;
import ru.cft.template.exception.BadTransactionException;
import ru.cft.template.mapper.TransferMapper;
import ru.cft.template.model.enums.TransferStatus;
import ru.cft.template.model.enums.TransferType;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final UserServiceImpl userService;
    private final WalletRepository walletRepository;

    //region<Методы перевода по номеру телефона>
    @Override
    public TransferResponse createTransferByRecipientPhone(Authentication authentication, TransferByPhoneBody body) {
        User user = userService.getUserByAuthentication(authentication);
        Wallet senderWallet = user.getWallet();

        Transfer transfer = new Transfer();

        transfer.setAmount(body.amount());
        transfer.setTransferDateTime(LocalDateTime.now());
        transfer.setStatus(TransferStatus.SUCCESSFUL);
        transfer.setSenderWallet(senderWallet);
        transfer.setSenderId(user.getId());

        if (body.recipientPhone() != null) {
            createTransferByPhone(transfer, body, senderWallet);
        } else {
            throw new BadTransactionException("Invalid transfer request");
        }

        senderWallet.setAmount(senderWallet.getAmount() - body.amount());
        senderWallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(senderWallet);

        transfer.setStatus(TransferStatus.SUCCESSFUL);
        transferRepository.save(transfer);

        return TransferMapper.mapTransferResponse(transfer);
    }

    private void createTransferByPhone(Transfer transaction, TransferByPhoneBody body, Wallet senderWallet) {
        transaction.setType(TransferType.TRANSFER);
        Wallet recipientWallet = userService.findUserByPhone(body.recipientPhone()).getWallet();
        transaction.setRecipientWallet(recipientWallet);

        if (recipientWallet == null) {
            throw new BadTransactionException("Receiver wallet not found");
        }

        GetValidTransactionByPhone(body, senderWallet, transaction);

        transaction.setRecipientId(userService.findUserByPhone(body.recipientPhone()).getId());
        transaction.setRecipientPhone(userService.findUserByPhone(body.recipientPhone()).getPhone());
        recipientWallet.setAmount(recipientWallet.getAmount() + body.amount());
        recipientWallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(recipientWallet);
    }

    private void GetValidTransactionByPhone(TransferByPhoneBody body, Wallet senderWallet, Transfer transfer) {
        if (!isValidTransferByPhone(body)) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new BadTransactionException("Invalid transaction request");
        }

        if (!isValidWalletForTransferByPhone(body, senderWallet)) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new BadTransactionException("Insufficient funds or wallet not found");
        }
    }

    private boolean isValidWalletForTransferByPhone(TransferByPhoneBody body, Wallet senderWallet) {
        return senderWallet != null && senderWallet.getAmount() >= body.amount();
    }

    private boolean isValidTransferByPhone(TransferByPhoneBody body) {
        if (body.amount() == null || body.amount() <= 0) {
            return false;
        }

        return (body.recipientPhone() != null);
    }
//endregion


    //region<Методы перевода по ID>
    @Override
    public TransferResponse createTransferByRecipientId(Authentication authentication, TransferByIdBody body) {
        User user = userService.getUserByAuthentication(authentication);
        Wallet senderWallet = user.getWallet();

        Transfer transfer = new Transfer();

        transfer.setAmount(body.amount());
        transfer.setTransferDateTime(LocalDateTime.now());
        transfer.setStatus(TransferStatus.SUCCESSFUL);
        transfer.setSenderWallet(senderWallet);
        transfer.setSenderId(user.getId());

        if (body.recipientId() != null) {
            createTransferById(transfer, body, senderWallet);
        } else {
            throw new BadTransactionException("Invalid transfer request");
        }

        senderWallet.setAmount(senderWallet.getAmount() - body.amount());
        senderWallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(senderWallet);

        transfer.setStatus(TransferStatus.SUCCESSFUL);
        transferRepository.save(transfer);

        return TransferMapper.mapTransferResponse(transfer);
    }

    private void createTransferById(Transfer transaction, TransferByIdBody body, Wallet senderWallet) {
        transaction.setType(TransferType.TRANSFER);
        Wallet receiverWallet = userService.getUserById(UUID.fromString(body.recipientId())).getWallet();
        transaction.setRecipientWallet(receiverWallet);

        if (receiverWallet == null) {
            throw new BadTransactionException("Receiver wallet not found");
        }

        GetValidTransactionById(body, senderWallet, transaction);

        transaction.setRecipientId(UUID.fromString(body.recipientId()));
        transaction.setRecipientPhone(userService.findUserById(UUID.fromString(body.recipientId())).phone());
        receiverWallet.setAmount(receiverWallet.getAmount() + body.amount());
        receiverWallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(receiverWallet);
    }

    private void GetValidTransactionById(TransferByIdBody body, Wallet senderWallet, Transfer transfer) {
        if (!isValidTransferById(body)) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new BadTransactionException("Invalid transaction request");
        }

        if (!isValidWalletForTransferById(body, senderWallet)) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new BadTransactionException("Insufficient funds or wallet not found");
        }
    }

    private boolean isValidWalletForTransferById(TransferByIdBody body, Wallet senderWallet) {
        return senderWallet != null && senderWallet.getAmount() >= body.amount();
    }

    private boolean isValidTransferById(TransferByIdBody body) {
        if (body.amount() == null || body.amount() <= 0) {
            return false;
        }

        return (body.recipientId() != null);
    }
//endregion


    //region<Методы перевода по инвойсу>
    @Override
    public TransferResponse createTransferByInvoice(Authentication authentication, TransferByInvoiceBody body) {
        return null;
    }
//endregion


    //region<Методы пополнения кошелька>
    @Override
    public WalletShortResponse hesoyam(Authentication authentication, AmountBody body) {

        if (body.amount() == null) {
            throw new BadTransactionException("You need to specify the amount to hesoyam from 1 to 1000 m.u.");
        }

        if (body.amount() > 1000) {
            throw new BadTransactionException("Too much amount to hesoyam, we poor company and don't have enough money. " + "Please specify the amount no more than 1000 m.u.");
        }

        User user = userService.getUserByAuthentication(authentication);
        Wallet recipientWallet = user.getWallet();

        List<Transfer> lastRefillTransactions = transferRepository.findLastTransactionsByTypeAndWalletId(TransferType.REPLENISHMENT, recipientWallet.getId(), PageRequest.of(0, 4));

        Transfer refillTransaction = new Transfer();
        refillTransaction.setRecipientWallet(recipientWallet);
        refillTransaction.setType(TransferType.REPLENISHMENT);
        refillTransaction.setRecipientId(user.getId());
        refillTransaction.setTransferDateTime(LocalDateTime.now());
        refillTransaction.setStatus(replenishment(lastRefillTransactions) ? TransferStatus.SUCCESSFUL : TransferStatus.FAILED);

        if (TransferStatus.SUCCESSFUL.equals(refillTransaction.getStatus())) {
            recipientWallet.setAmount(recipientWallet.getAmount() + body.amount());
            refillTransaction.setAmount(body.amount());
            recipientWallet.setLastUpdate(LocalDateTime.now());
            walletRepository.save(recipientWallet);
        } else {
            refillTransaction.setAmount(body.amount());
        }

        transferRepository.save(refillTransaction);

        return new WalletShortResponse(recipientWallet.getId(), recipientWallet.getAmount());
    }

    @Override
    public WalletShortResponse casino(Authentication authentication, AmountBody body) {
        if (body.amount() == null) {
            throw new BadTransactionException("You need to specify the amount to hesoyam from 1 to 1000 m.u.");
        }

        if (body.amount() > 1000) {
            throw new BadTransactionException("Too much amount to hesoyam, we poor company and don't have enough money. " + "Please specify the amount no more than 1000 m.u.");
        }

        User user = userService.getUserByAuthentication(authentication);
        Wallet recipientWallet = user.getWallet();

        if (recipientWallet.getAmount() >= body.amount()) {
            List<Transfer> lastRefillTransactions = transferRepository.findLastTransactionsByTypeAndWalletId(TransferType.REPLENISHMENT, recipientWallet.getId(), PageRequest.of(0, 4));

            Transfer refillTransaction = new Transfer();
            refillTransaction.setRecipientWallet(recipientWallet);
            refillTransaction.setRecipientId(user.getId());
            refillTransaction.setType(TransferType.REPLENISHMENT);
            refillTransaction.setTransferDateTime(LocalDateTime.now());
            refillTransaction.setStatus(replenishment(lastRefillTransactions) ? TransferStatus.SUCCESSFUL : TransferStatus.FAILED);

            if (TransferStatus.SUCCESSFUL.equals(refillTransaction.getStatus())) {
                recipientWallet.setAmount(recipientWallet.getAmount() + body.amount() * 2);
                refillTransaction.setAmount(body.amount());
                recipientWallet.setLastUpdate(LocalDateTime.now());
                walletRepository.save(recipientWallet);
            } else {
                recipientWallet.setAmount(recipientWallet.getAmount() - body.amount());
                refillTransaction.setAmount(body.amount());
                recipientWallet.setLastUpdate(LocalDateTime.now());
                walletRepository.save(recipientWallet);
            }

            transferRepository.save(refillTransaction);
        } else {
            throw new BadTransactionException("You don't have enough money for bet");
        }

        return new WalletShortResponse(recipientWallet.getId(), recipientWallet.getAmount());
    }

    private boolean replenishment(List<Transfer> transfers) {
        Random random = new Random();
        if (transfers.size() >= 3 && transfers.stream().filter(t -> TransferStatus.FAILED.equals(t.getStatus())).count() >= 3) {
            return true;
        }
        return random.nextInt(4) == 0;
    }
//endregion


    //region<Геты>
    @Override
    public List<TransferResponse> getAllTransfers(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);

        List<Transfer> transfers = transferRepository.findAllBySenderId(user.getId());
        return transfers.stream()
                .map(TransferMapper::mapTransferResponse)
                .toList();
    }

    @Override
    public List<TransferResponse> getAllTransfersByRecipientId(Authentication authentication, UUID recipientId) {
        User user = userService.getUserByAuthentication(authentication);

        if (user != null) {
            List<Transfer> transfers = transferRepository.findAllByRecipientId(recipientId);
            return transfers.stream()
                    .map(TransferMapper::mapTransferResponse)
                    .toList();
        }

        throw new AccessRightsException("You don't have enough rights to access this account");
    }

    @Override
    public List<TransferResponse> getAllTransfersBySenderWallet(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);

        if (user != null) {
            List<Transfer> transfers = transferRepository.findBySenderWallet(user.getWallet());
            return transfers.stream()
                    .map(TransferMapper::mapTransferResponse)
                    .toList();
        }

        throw new AccessRightsException("You don't have enough rights to access this account");
    }

    @Override
    public List<TransferResponse> getAllTransfersByType(Authentication authentication, TransferType type) {
        User user = userService.getUserByAuthentication(authentication);

        if (user != null) {
            List<Transfer> transfers = transferRepository.findAllByType(type);
            return transfers.stream()
                    .map(TransferMapper::mapTransferResponse)
                    .toList();
        }

        throw new AccessRightsException("You don't have enough rights to access this account");
    }
//endregion

}
