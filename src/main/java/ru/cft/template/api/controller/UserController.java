package ru.cft.template.api.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.api.dto.UserCreateRequest;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.service.UserService;

@RequestMapping("/potato/api/users")
@Slf4j
public class UserController {

    @GetMapping("/{userID}")
    public User getByID(@PathVariable int userID){
        log.info("Success");
        return null;
    }

    @GetMapping("/findByEmail")
    public User getByEmail(@PathVariable String email){
        log.info("Success");
        return null;
    }

    @GetMapping("/findByPhoneNumber")
    public User getByPhoneNumber(@PathVariable String phoneNumber){
        log.info("Success");
        return null;
    }

    @PostMapping
    public User create(@Valid @RequestBody UserCreateRequest user){
        log.info("Success");
        return null;
    }

    @PatchMapping
    public User update(@PathVariable int userID){
        log.info("Success");
        return null;
    }
}
