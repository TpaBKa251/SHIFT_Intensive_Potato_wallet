package ru.cft.template.model.response;

public record UserInfoResponse(
        String firstName,
        String lastName,
        String middleName,
        Long phone,
        String email
) {
}
