package ru.cft.template.model.request;

public record TransferByIdBody(
        String recipientId,
        Long amount
) {
}
