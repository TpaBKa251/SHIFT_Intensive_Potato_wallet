package ru.cft.template.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.request.AmountBody;
import ru.cft.template.model.request.TransferByPhoneBody;
import ru.cft.template.model.response.TransferResponse;
import ru.cft.template.model.response.WalletShortResponse;
import ru.cft.template.service.TransferService;

import java.util.List;

@RestController
@RequestMapping("potato/api/users/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/hesoyam")
    public ResponseEntity<WalletShortResponse> hesoyam(Authentication authentication, @RequestBody AmountBody body){
        return ResponseEntity.ok(transferService.hesoyam(authentication, body));
    }

    @PostMapping("/casino")
    public ResponseEntity<WalletShortResponse> casino(Authentication authentication, @RequestBody AmountBody body){
        return ResponseEntity.ok(transferService.casino(authentication, body));
    }

    @PostMapping()
    public ResponseEntity<TransferResponse> createTransferByPhone(
            Authentication authentication,
            @RequestBody TransferByPhoneBody body
    ){
        return ResponseEntity.ok(transferService.createTransferByRecipientPhone(authentication, body));
    }
}
