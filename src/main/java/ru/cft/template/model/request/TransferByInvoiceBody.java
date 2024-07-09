package ru.cft.template.model.request;

import java.util.UUID;

public record TransferByInvoiceBody(
        String invoiceId
) {
}
