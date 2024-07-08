package ru.cft.template.model.request;

public record LoginByPhoneBody(
     Long phone,
     String password
) {
}
