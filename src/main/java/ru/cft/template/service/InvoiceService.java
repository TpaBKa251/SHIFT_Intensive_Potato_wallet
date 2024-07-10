package ru.cft.template.service;

import org.springframework.security.core.Authentication;
import ru.cft.template.model.request.CreateInvoice;
import ru.cft.template.model.response.InvoiceResponse;

import java.util.List;

public interface InvoiceService {
    InvoiceResponse createInvoice(Authentication authentication, CreateInvoice body);

    List<InvoiceResponse> getAllInvoices(Authentication authentication);
}
