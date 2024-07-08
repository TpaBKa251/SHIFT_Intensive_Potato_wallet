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
import ru.cft.template.model.response.UserInfoResponse;
import ru.cft.template.model.response.UserResponse;
import ru.cft.template.service.impl.UserServiceImpl;

@RestController
@RequestMapping("potato/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping()
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegisterBody body) {
        log.info("Registering user: {}", body);
        return ResponseEntity.ok(userService.registerUser(body));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUser(Authentication authentication) {
        log.info("kjdasfh");
        return ResponseEntity.ok(userService.getUserResponseByAuthentication(authentication));
    }

    @PatchMapping("/edit")
    public ResponseEntity<UserResponse> updateUser(Authentication authentication, @RequestBody UserUpdateBody body){
        return ResponseEntity.ok(userService.updateUser(authentication, body));
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<UserInfoResponse> getUser(@PathVariable Long phoneNumber){
        log.info("Получено");
        return ResponseEntity.ok(userService.getUserByPhone(phoneNumber));
    }

    /*@GetMapping("/{email}")
    public ResponseEntity<UserInfoResponse> getUser(@PathVariable String email){
        log.info("Получено");
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }*/
}
