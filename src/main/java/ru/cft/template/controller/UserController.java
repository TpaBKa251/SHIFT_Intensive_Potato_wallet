package ru.cft.template.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.entity.User;
import ru.cft.template.model.request.RegisterBody;
import ru.cft.template.model.request.UserUpdateBody;
import ru.cft.template.model.response.TokenResponse;
import ru.cft.template.model.response.UserResponse;
import ru.cft.template.service.serviceimpl.UserServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("potato/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<TokenResponse> addUser(@RequestBody RegisterBody registerBody) {
        log.info("User added");
        return ResponseEntity.ok(userService.registerUser(registerBody));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(Authentication authentication) {
        log.info("Here is your user profile");
        return ResponseEntity.ok(userService.getUserResponseByAuthentication(authentication));
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<User> getUserById(@PathVariable Long phoneNumber) {
        log.info("Here is the user with phone " + phoneNumber);
        return ResponseEntity.ok(userService.findUserByPhone(phoneNumber));
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        log.info("Here is the user with email " + email);
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id){
        log.info("Here is the user with id " + id);
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PatchMapping("/edit")
    public ResponseEntity<UserResponse> updateUser(Authentication authentication, @RequestBody UserUpdateBody body) {
        log.info("User updated (edited)");
        return ResponseEntity.ok(userService.updateUser(authentication, body));
    }
}
