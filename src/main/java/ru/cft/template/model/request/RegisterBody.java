package ru.cft.template.model.request;

public record RegisterBody(
        Long phoneNumber,
        String password,
        String firstName,
        String lastName,
        String middleName,
        String email,
        Integer age
) {
}
