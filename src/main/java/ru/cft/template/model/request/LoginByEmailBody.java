package ru.cft.template.model.request;

public record LoginByEmailBody(
        String email,
        String password
) {
}
