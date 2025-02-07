package ru.cft.template.service;

import org.springframework.security.core.Authentication;
import ru.cft.template.model.request.CreateInvoice;
import ru.cft.template.model.response.InvoiceResponse;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    InvoiceResponse createInvoice(Authentication authentication, CreateInvoice body);

    List<InvoiceResponse> getAllInvoices(Authentication authentication);

    List<InvoiceResponse> getAllIncomingInvoices(Authentication authentication);

    InvoiceResponse cancelInvoice(Authentication authentication, UUID invoiceId);

    InvoiceResponse getInvoice(Authentication authentication, UUID invoiceId);

    List<InvoiceResponse> getAllOutgoingInvoices(Authentication authentication);

    InvoiceResponse getLastIncomingInvoice(Authentication authentication);

    InvoiceResponse getFirstIncomingInvoice(Authentication authentication);

    Long getTotalInvoices(Authentication authentication);
}
