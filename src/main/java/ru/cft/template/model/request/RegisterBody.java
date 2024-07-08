package ru.cft.template.model.request;

import java.util.Date;

public record RegisterBody(
        Long phone,
        String password,
        String firstName,
        String lastName,
        String email,
        Date birthDate
) {
}
