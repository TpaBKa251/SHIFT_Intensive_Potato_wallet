package ru.cft.template.mapper;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.cft.template.config.SecurityConfig;
import ru.cft.template.entity.User;
import ru.cft.template.model.request.RegisterBody;
import ru.cft.template.model.response.UserInfoResponse;
import ru.cft.template.model.response.UserResponse;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User mapRegisterBodyToUser(RegisterBody body) {
        User user = new User();
        user.setBirthDate(body.birthDate());
        user.setPassword(passwordEncoder.encode(body.password()));
        user.setFirstName(body.firstName());
        user.setLastName(body.lastName());
        user.setMiddleName(body.middleName());
        user.setEmail(body.email());
        user.setPhone(body.phone());
        return user;
    }

    public static UserResponse mapUserToResponse(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getWallet().getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getFirstName() + " " + user.getLastName() + " " + user.getMiddleName(),
                user.getEmail(),
                user.getPhone(),
                user.getRegistrationDate(),
                user.getLastUpdateDate(),
                user.getBirthDate()
        );
    }

    public static UserInfoResponse mapUserInfoToResponse(User user) {
        return new UserInfoResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getPhone(),
                user.getEmail()
        );
    }
}
