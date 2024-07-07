package ru.cft.template.model.response;

import java.util.Date;

public record UserResponse(
        String id,
        String walletID,
        String firstName,
        String lastName,
        //String middleName,
        String email,
        Long phoneNumber,
        Date registrationDate,
        Date lastUpdateDate,
        Integer age
) {
}
