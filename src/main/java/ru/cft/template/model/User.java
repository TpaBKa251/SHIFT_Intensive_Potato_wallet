package ru.cft.template.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public class User {
    private long id;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String email;
    private final String phoneNumber;
    private final String birthDate;
    private final String password;

}
