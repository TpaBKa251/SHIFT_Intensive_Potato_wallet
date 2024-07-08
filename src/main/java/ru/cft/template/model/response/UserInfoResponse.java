package ru.cft.template.model.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
public record UserInfoResponse(
        String firstName,
        String lastName,
        String middleName,
        Long phone,
        String email
) {
}
