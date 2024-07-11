package ru.cft.template.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.request.RegisterBody;
import ru.cft.template.model.request.UserUpdateBody;
import ru.cft.template.model.response.TokenResponse;
import ru.cft.template.model.response.UserInfoResponse;
import ru.cft.template.model.response.UserResponse;
import ru.cft.template.service.impl.UserServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("potato/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping()
    public TokenResponse registerUser(@RequestBody @Valid RegisterBody body) {
        return userService.registerUser(body);
    }

    @GetMapping("/profile")
    public UserResponse getUser(Authentication authentication) {
        log.info("Получено");
        return userService.getUserResponseByAuthentication(authentication);
    }

    @PatchMapping("/edit")
    public UserResponse updateUser(Authentication authentication, @RequestBody UserUpdateBody body) {
        return userService.updateUser(authentication, body);
    }

    @GetMapping("/findviaphone/{phoneNumber}")
    public UserInfoResponse getUserByPhone(@PathVariable Long phoneNumber) {
        log.info("Получено");
        return userService.getUserByPhone(phoneNumber);
    }

    @GetMapping("/findviaemail/{email}")
    public UserInfoResponse getUserByEmail(@PathVariable String email){
        log.info("Получено");
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{id}")
    public UserInfoResponse getUserById(@PathVariable String id){
        UUID uuid = UUID.fromString(id);

        return userService.findUserById(uuid);
    }
}
