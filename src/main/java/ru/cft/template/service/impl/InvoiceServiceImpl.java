package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.Invoice;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.exception.AccessRightsException;
import ru.cft.template.mapper.InvoiceMapper;
import ru.cft.template.model.enums.InvoiceStatus;
import ru.cft.template.model.enums.InvoiceType;
import ru.cft.template.model.request.CreateInvoice;
import ru.cft.template.model.response.InvoiceResponse;
import ru.cft.template.repository.InvoiceRepository;
import ru.cft.template.repository.WalletRepository;
import ru.cft.template.service.InvoiceService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserServiceImpl userService;
    private final WalletRepository walletRepository;
    private final JdbcTemplate jdbcTemplate;


    @Override
    public InvoiceResponse createInvoice(Authentication authentication, CreateInvoice body) {
        if (userService.getUserByAuthentication(authentication).getId().equals(body.recipientId())){
            throw new IllegalArgumentException("You cannot create an invoice to yourself");
        }

        long cnt = jdbcTemplate.query("select nextval('invoices_number_seq')", rs -> {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("Unable to retrieve value from sequence chessgame_seq.");
            }
        });

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
        senderInvoice.setInvoiceHolder(sender);
        senderInvoice.setInvoiceNumber(cnt);


        recipientInvoice.setSender(sender);
        recipientInvoice.setRecipient(recipient);
        recipientInvoice.setSenderWallet(senderWallet);
        recipientInvoice.setReceiverWallet(recipientWallet);
        recipientInvoice.setAmount(body.amount());
        recipientInvoice.setComment(body.comment());
        recipientInvoice.setType(InvoiceType.INCOMING);
        recipientInvoice.setStatus(InvoiceStatus.UNPAID);
        recipientInvoice.setInvoiceDateTime(LocalDateTime.now());
        recipientInvoice.setInvoiceHolder(recipient);
        recipientInvoice.setInvoiceNumber(cnt);

        invoiceRepository.save(senderInvoice);
        invoiceRepository.save(recipientInvoice);


        return InvoiceMapper.mapInvoiceResponse(senderInvoice);
    }


    @Override
    public List<InvoiceResponse> getAllInvoices(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);
        List<Invoice> invoices = invoiceRepository.findByInvoiceHolder(user);

        return invoices.stream()
                .map(InvoiceMapper::mapInvoiceResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> getAllIncomingInvoices(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);
        List<Invoice> invoices = invoiceRepository.findByRecipientId(user.getId());

        return invoices.stream()
                .map(InvoiceMapper::mapInvoiceResponse)
                .toList();
    }

    @Override
    public InvoiceResponse cancelInvoice(Authentication authentication, UUID invoiceId) {
        User user = userService.getUserByAuthentication(authentication);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        assert invoice != null;
        Invoice incomingInvoice = invoiceRepository.finByInvoiceHolderIdAndInvoiceNumber(invoice.getRecipient().getId(), invoice.getInvoiceNumber());

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new IllegalArgumentException("Invoice has been cancelled.");
        }

        if (invoice.getSender() != invoice.getInvoiceHolder()){
            throw new IllegalArgumentException("You can't cancel this invoice.");
        }

        invoice.setStatus(InvoiceStatus.CANCELLED);
        invoice.setCancelDateTime(LocalDateTime.now());
        invoiceRepository.save(invoice);

        incomingInvoice.setStatus(InvoiceStatus.CANCELLED);
        incomingInvoice.setCancelDateTime(LocalDateTime.now());
        invoiceRepository.save(incomingInvoice);

        return InvoiceMapper.mapInvoiceResponse(invoice);
    }

    @Override
    public InvoiceResponse getInvoice(Authentication authentication, UUID invoiceId) {
        User user = userService.getUserByAuthentication(authentication);
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);

        if (invoice.getInvoiceHolder().getId().equals(user.getId())) {
            return InvoiceMapper.mapInvoiceResponse(invoice);
        }
        else {
            throw new AccessRightsException("You do not have the right to access this resource.");
        }
    }

    @Override
    public List<InvoiceResponse> getAllOutgoingInvoices(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);
        List<Invoice> invoices = invoiceRepository.findBySenderId(user.getId());

        return invoices.stream()
                .map(InvoiceMapper::mapInvoiceResponse)
                .toList();
    }

    @Override
    public InvoiceResponse getLastIncomingInvoice(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);

        Invoice invoice = invoiceRepository.findLastInvoiceByInvoiceHolderId(user.getId());

        if (invoice == null){
            throw new RuntimeException("Invoice not found");
        }
        return InvoiceMapper.mapInvoiceResponse(invoice);
    }

    @Override
    public InvoiceResponse getFirstIncomingInvoice(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);

        Invoice invoice = invoiceRepository.findFirstInvoiceByInvoiceHolderId(user.getId());

        if (invoice == null){
            throw new RuntimeException("Invoice not found");
        }
        return InvoiceMapper.mapInvoiceResponse(invoice);
    }

    @Override
    public Long getTotalInvoices(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);
        List<Invoice> invoices = invoiceRepository.findByRecipientId(user.getId());

        long totalInvoices = 0L;
        for (Invoice invoice : invoices) {
            if (invoice.getStatus() == InvoiceStatus.UNPAID) {
                totalInvoices += invoice.getAmount();
            }
        }

        return totalInvoices;
    }
}
