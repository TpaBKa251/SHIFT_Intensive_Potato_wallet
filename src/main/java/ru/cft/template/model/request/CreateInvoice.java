package ru.cft.template.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateInvoice(
        @NotNull(message = "Please enter the recipient ID")
        UUID recipientId,

        @Size(max = 255, message = "Message must be no more than 255 characters")
        String comment,

        @NotNull(message = "Please enter the amount")
        Long amount
) {
}
