package ru.cft.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.model.response.WalletResponse;
import ru.cft.template.service.WalletService;

@RestController
@RequestMapping("potato/api")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/wallet")
    public WalletResponse getUser(Authentication authentication) {
        return walletService.getUserWallet(authentication);
    }

}
