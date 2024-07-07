package ru.cft.template.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.entity.User;
import ru.cft.template.model.request.RegisterBody;
import ru.cft.template.model.response.UserResponse;

@Component
public class UserMapper {
    public static User mapRegisterBodyToUser(RegisterBody registerBody) {
        User user = new User();
        user.setAge(registerBody.age());
        user.setEmail(registerBody.email());
        user.setFirstName(registerBody.firstName());
        user.setLastName(registerBody.lastName());
        user.setPassword(registerBody.password());
        user.setMiddleName(registerBody.middleName());
        user.setPhoneNumber(registerBody.phoneNumber());

        return user;
    }

    public static UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getWallet().getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRegistrationDate(),
                user.getLastUpdateDate(),
                user.getAge()
        );
    }
}
