package ru.cft.template.model.request;

import java.util.Date;

public record UserUpdateBody(
        String firstName,
        String lastName,
        String middleName,
        Integer age
) {
}
