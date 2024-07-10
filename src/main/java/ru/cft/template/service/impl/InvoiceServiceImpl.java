package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.Invoice;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.mapper.InvoiceMapper;
import ru.cft.template.model.enums.InvoiceStatus;
import ru.cft.template.model.enums.InvoiceType;
import ru.cft.template.model.request.CreateInvoice;
import ru.cft.template.model.response.InvoiceResponse;
import ru.cft.template.repository.InvoiceRepository;
import ru.cft.template.repository.WalletRepository;
import ru.cft.template.service.InvoiceService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserServiceImpl userService;
    private final WalletRepository walletRepository;


    @Override
    public InvoiceResponse createInvoice(Authentication authentication, CreateInvoice body) {
        User sender = userService.getUserByAuthentication(authentication);
        User recipient = userService.getUserById(body.recipientId());
        Wallet senderWallet = sender.getWallet();
        Wallet recipientWallet = recipient.getWallet();

        Invoice senderInvoice = new Invoice();
        Invoice recipientInvoice = new Invoice();

        senderInvoice.setSender(sender);
        senderInvoice.setRecipient(recipient);
        senderInvoice.setSenderWallet(senderWallet);
        senderInvoice.setReceiverWallet(recipientWallet);
        senderInvoice.setAmount(body.amount());
        senderInvoice.setComment(body.comment());
        senderInvoice.setType(InvoiceType.OUTGOING);
        senderInvoice.setStatus(InvoiceStatus.UNPAID);
        senderInvoice.setInvoiceDateTime(LocalDateTime.now());

        //ПОМЕНЯТЬ!!!
        senderInvoice.setInvoiceNumber(1L);

        recipientInvoice.setSender(sender);
        recipientInvoice.setRecipient(recipient);
        recipientInvoice.setSenderWallet(senderWallet);
        recipientInvoice.setReceiverWallet(recipientWallet);
        recipientInvoice.setAmount(body.amount());
        recipientInvoice.setComment(body.comment());
        recipientInvoice.setType(InvoiceType.INCOMING);
        recipientInvoice.setStatus(InvoiceStatus.UNPAID);
        recipientInvoice.setInvoiceDateTime(LocalDateTime.now());

        //ПОМЕНЯТЬ!!!
        recipientInvoice.setInvoiceNumber(2L);

        invoiceRepository.save(senderInvoice);
        invoiceRepository.save(recipientInvoice);

        return InvoiceMapper.mapInvoiceResponse(senderInvoice);
    }


    @Override
    public List<InvoiceResponse> getAllInvoices(Authentication authentication) {
        return List.of();
    }
}
