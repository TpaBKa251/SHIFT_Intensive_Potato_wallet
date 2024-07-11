package ru.cft.template.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransferByIdBody(
        @NotBlank(message = "Please enter the recipient ID")
        String recipientId,

        @NotNull(message = "Please enter the amount")
        Long amount
) {
}
