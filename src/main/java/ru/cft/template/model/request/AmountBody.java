package ru.cft.template.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AmountBody(
        @NotNull(message = "Please enter the amount")
        Long amount) {
}
