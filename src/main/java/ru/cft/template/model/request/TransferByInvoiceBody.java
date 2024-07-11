package ru.cft.template.model.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TransferByInvoiceBody(
        @NotNull(message = "Please enter the invoice ID")
        UUID invoiceId
) {
}
