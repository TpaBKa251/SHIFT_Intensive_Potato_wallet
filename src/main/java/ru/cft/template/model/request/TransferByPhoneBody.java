package ru.cft.template.model.request;

import jakarta.validation.constraints.NotNull;

public record TransferByPhoneBody(
        @NotNull(message = "Please enter the recipient phone")
        Long recipientPhone,

        @NotNull(message = "Please enter the amount")
        Long amount
) {
}
