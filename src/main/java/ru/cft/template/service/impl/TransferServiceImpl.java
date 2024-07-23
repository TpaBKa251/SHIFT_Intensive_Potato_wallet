package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.Invoice;
import ru.cft.template.entity.Transfer;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.exception.AccessRightsException;
import ru.cft.template.exception.BadTransactionException;
import ru.cft.template.mapper.TransferMapper;
import ru.cft.template.model.enums.InvoiceStatus;
import ru.cft.template.model.enums.InvoiceType;
import ru.cft.template.model.enums.TransferStatus;
import ru.cft.template.model.enums.TransferType;
import ru.cft.template.model.request.AmountBody;
import ru.cft.template.model.request.TransferByIdBody;
import ru.cft.template.model.request.TransferByInvoiceBody;
import ru.cft.template.model.request.TransferByPhoneBody;
import ru.cft.template.model.response.TransactionHistoryResponse;
import ru.cft.template.model.response.TransferResponse;
import ru.cft.template.model.response.WalletShortResponse;
import ru.cft.template.repository.InvoiceRepository;
import ru.cft.template.repository.TransferRepository;
import ru.cft.template.repository.WalletRepository;
import ru.cft.template.service.InvoiceService;
import ru.cft.template.service.TransferService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final UserServiceImpl userService;
    private final WalletRepository walletRepository;
    private final InvoiceRepository invoiceRepository;

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
        Transfer recipientTransfer = transaction;
        recipientTransfer.setType(TransferType.REPLENISHMENT);
        transaction.setType(TransferType.TRANSFER);
        Wallet recipientWallet = userService.findUserByPhone(body.recipientPhone()).getWallet();
        transaction.setRecipientWallet(recipientWallet);
        recipientTransfer.setRecipientWallet(recipientWallet);

        if (recipientWallet == null) {
            throw new BadTransactionException("Receiver wallet not found");
        }

        GetValidTransactionByPhone(body, senderWallet, transaction);

        transaction.setRecipientId(userService.findUserByPhone(body.recipientPhone()).getId());
        recipientTransfer.setRecipientId(userService.findUserByPhone(body.recipientPhone()).getId());
        transaction.setRecipientPhone(userService.findUserByPhone(body.recipientPhone()).getPhone());
        recipientTransfer.setRecipientPhone(userService.findUserByPhone(body.recipientPhone()).getPhone());
        recipientTransfer.setStatus(TransferStatus.SUCCESSFUL);
        recipientWallet.setAmount(recipientWallet.getAmount() + body.amount());
        recipientWallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(recipientWallet);
        transferRepository.save(recipientTransfer);
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
        Transfer recipientTransfer = transaction;
        recipientTransfer.setType(TransferType.REPLENISHMENT);
        transaction.setType(TransferType.TRANSFER);
        Wallet receiverWallet = userService.getUserById(UUID.fromString(body.recipientId())).getWallet();
        transaction.setRecipientWallet(receiverWallet);
        recipientTransfer.setRecipientWallet(receiverWallet);

        if (receiverWallet == null) {
            throw new BadTransactionException("Receiver wallet not found");
        }

        GetValidTransactionById(body, senderWallet, transaction);

        transaction.setRecipientId(UUID.fromString(body.recipientId()));
        recipientTransfer.setRecipientId(UUID.fromString(body.recipientId()));
        transaction.setRecipientPhone(userService.findUserById(UUID.fromString(body.recipientId())).phone());
        recipientTransfer.setRecipientPhone(userService.findUserById(UUID.fromString(body.recipientId())).phone());
        recipientTransfer.setStatus(TransferStatus.SUCCESSFUL);
        receiverWallet.setAmount(receiverWallet.getAmount() + body.amount());
        receiverWallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(receiverWallet);
        transferRepository.save(recipientTransfer);
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
        User invoiceRecipientWhichPayTheInvoice = userService.getUserByAuthentication(authentication);
        Wallet payManWallet = invoiceRecipientWhichPayTheInvoice.getWallet();

        Transfer transfer = new Transfer();
        Invoice invoiceToPay = invoiceRepository.findById(body.invoiceId()).orElse(null);

        assert invoiceToPay != null;

        if (invoiceToPay.getStatus() == InvoiceStatus.PAID) {
            throw new BadTransactionException("Invoice already payed");
        }
        else if (invoiceToPay.getType() == InvoiceType.OUTGOING){
            throw new BadTransactionException("You is trying to pay an outgoing invoice");
        }
        else if (invoiceToPay.getStatus() == InvoiceStatus.CANCELLED){
            throw new BadTransactionException("Invoice has been cancelled");
        }

        User invoiceSenderWhichGetPayForInvoice = userService.getUserById(invoiceToPay.getSender().getId());

        Invoice invoiceOutGoing = invoiceRepository.finByInvoiceHolderIdAndInvoiceNumber(invoiceSenderWhichGetPayForInvoice.getId(), invoiceToPay.getInvoiceNumber());

        transfer.setAmount(invoiceToPay.getAmount());
        transfer.setTransferDateTime(LocalDateTime.now());
        transfer.setStatus(TransferStatus.SUCCESSFUL);
        transfer.setSenderWallet(payManWallet);
        transfer.setSenderId(invoiceRecipientWhichPayTheInvoice.getId());
        transfer.setInvoiceId(body.invoiceId());

        createTransferByInvoice(transfer, body, payManWallet);

        payManWallet.setAmount(payManWallet.getAmount() - invoiceToPay.getAmount());
        payManWallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(payManWallet);

        invoiceToPay.setStatus(InvoiceStatus.PAID);
        invoiceToPay.setPayDateTime(LocalDateTime.now());
        invoiceRepository.save(invoiceToPay);

        invoiceOutGoing.setStatus(InvoiceStatus.PAID);
        invoiceOutGoing.setPayDateTime(LocalDateTime.now());
        invoiceRepository.save(invoiceOutGoing);

        transfer.setStatus(TransferStatus.SUCCESSFUL);
        transferRepository.save(transfer);

        return TransferMapper.mapTransferResponse(transfer);
    }

    private void createTransferByInvoice(Transfer transaction, TransferByInvoiceBody body, Wallet senderWallet) {
        Transfer recipientTransfer = transaction;
        recipientTransfer.setType(TransferType.REPLENISHMENT);
        transaction.setType(TransferType.PAYMENT);
        Invoice invoiceToPay = invoiceRepository.findById(body.invoiceId()).orElse(null);
        assert invoiceToPay != null;

        User user = userService.getUserById(invoiceToPay.getSender().getId());

        Wallet getPayManWallet = user.getWallet();
        transaction.setRecipientWallet(getPayManWallet);
        recipientTransfer.setRecipientWallet(getPayManWallet);

        if (getPayManWallet == null) {
            throw new BadTransactionException("Receiver wallet not found");
        }

        GetValidTransactionByInvoice(body, senderWallet, transaction);

        transaction.setRecipientId(invoiceToPay.getSender().getId());
        recipientTransfer.setRecipientId(invoiceToPay.getSender().getId());
        transaction.setRecipientPhone(userService.findUserById(invoiceToPay.getSender().getId()).phone());
        recipientTransfer.setRecipientPhone(userService.findUserById(invoiceToPay.getSender().getId()).phone());
        recipientTransfer.setStatus(TransferStatus.SUCCESSFUL);
        getPayManWallet.setAmount(getPayManWallet.getAmount() + invoiceToPay.getAmount());
        getPayManWallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(getPayManWallet);
        transferRepository.save(recipientTransfer);
    }

    private void GetValidTransactionByInvoice(TransferByInvoiceBody body, Wallet senderWallet, Transfer transfer) {
        if (!isValidTransferByInvoice(body)) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new BadTransactionException("Invalid transaction request");
        }

        if (!isValidWalletForTransferByInvoice(body, senderWallet)) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new BadTransactionException("Insufficient funds or wallet not found");
        }
    }

    private boolean isValidWalletForTransferByInvoice(TransferByInvoiceBody body, Wallet senderWallet) {
        Invoice invoiceToPay = invoiceRepository.findById(body.invoiceId()).orElse(null);
        assert invoiceToPay != null;

        return senderWallet != null && senderWallet.getAmount() >= invoiceToPay.getAmount();
    }

    private boolean isValidTransferByInvoice(TransferByInvoiceBody body) {
        Invoice invoiceToPay = invoiceRepository.findById(body.invoiceId()).orElse(null);
        assert invoiceToPay != null;

        if (invoiceToPay.getAmount() == null || invoiceToPay.getAmount() <= 0) {
            return false;
        }

        return (body.invoiceId() != null);
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
        //refillTransaction.setSenderId(user.getId());
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

        return new WalletShortResponse(recipientWallet.getId(), recipientWallet.getAmount(), refillTransaction.getStatus().toString());
    }

    @Override
    public WalletShortResponse casino(Authentication authentication, AmountBody body) {
        if (body.amount() == null) {
            throw new BadTransactionException("You need to specify the amount to casino from 1 to 1000 m.u.");
        }

        if (body.amount() > 1000) {
            throw new BadTransactionException("Too much amount to casino, we poor company and don't have enough money. " + "Please specify the amount no more than 1000 m.u.");
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
            return new WalletShortResponse(recipientWallet.getId(), recipientWallet.getAmount(), refillTransaction.getStatus().toString());

        } else {
            throw new BadTransactionException("You don't have enough money for bet");
        }
    }

    @Override
    public WalletShortResponse roulette(Authentication authentication, AmountBody body, Long amount, Long min, Long max) {
        Random random = new Random();

        if (body.amount() == null) {
            throw new BadTransactionException("You need to specify the amount to hesoyam from 1 to 1000 m.u.");
        }

        if (body.amount() > 1000) {
            throw new BadTransactionException("Too much amount to hesoyam, we poor company and don't have enough money. " + "Please specify the amount no more than 1000 m.u.");
        }

        User user = userService.getUserByAuthentication(authentication);
        Wallet recipientWallet = user.getWallet();

        String s = "";

        if (recipientWallet.getAmount() >= body.amount()) {
            if (random.nextLong(min, max+1) == amount){
                recipientWallet.setAmount(recipientWallet.getAmount() + body.amount() * (max - min));
                walletRepository.save(recipientWallet);
                s = "Неудача!";
            }
            else {
                recipientWallet.setAmount(recipientWallet.getAmount() - body.amount());
                walletRepository.save(recipientWallet);
                s = "Успех";
            }
        } else {
            throw new BadTransactionException("You don't have enough money for bet");
        }

        return new WalletShortResponse(recipientWallet.getId(), recipientWallet.getAmount(), s);
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
        transfers.addAll(transferRepository.findByRecipientIdAndType(user.getId(), TransferType.REPLENISHMENT));
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

    public List<TransactionHistoryResponse> getHistory(Authentication authentication, TransferType type) {
        User user = userService.getUserByAuthentication(authentication);
        Wallet userWallet = user.getWallet();

        List<Transfer> transactions;
        if (type == null) {
            transactions = transferRepository.findBySenderWallet(userWallet);
        } else {
            transactions = transferRepository.findBySenderWalletAndType(userWallet, type);
        }

        return transactions.stream()
                .map(TransferMapper::mapTransactionToHistory)
                .collect(Collectors.toList());
    }
//endregion

}
