package ru.cft.template.model.request;

public record TransferByPhoneBody(
        Long recipientPhone,
        Long amount
) {
}
