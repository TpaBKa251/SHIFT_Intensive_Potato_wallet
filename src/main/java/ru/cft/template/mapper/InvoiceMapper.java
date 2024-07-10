package ru.cft.template.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.entity.Invoice;
import ru.cft.template.model.response.InvoiceResponse;

@Component
public class InvoiceMapper {

    public static InvoiceResponse mapInvoiceResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getSender().getId(),
                invoice.getRecipient().getId(),
                invoice.getAmount(),
                invoice.getInvoiceNumber(),
                invoice.getStatus(),
                invoice.getType(),
                invoice.getInvoiceDateTime(),
                invoice.getComment()
        );
    }
}
