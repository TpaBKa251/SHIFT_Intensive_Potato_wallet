package ru.cft.template.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class UserInfo {
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String email;
    private final String phoneNumber;
}
