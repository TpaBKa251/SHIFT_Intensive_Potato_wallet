package ru.cft.template.model.request;

import jakarta.validation.constraints.NotBlank;

public record AmountBody(
        @NotBlank(message = "You need to enter the amount")
        Long amount) {
}
