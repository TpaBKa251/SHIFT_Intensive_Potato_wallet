package ru.cft.template.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.TransferType;
import ru.cft.template.model.request.AmountBody;
import ru.cft.template.model.request.TransferByIdBody;
import ru.cft.template.model.request.TransferByInvoiceBody;
import ru.cft.template.model.request.TransferByPhoneBody;
import ru.cft.template.model.response.TransferResponse;
import ru.cft.template.model.response.WalletShortResponse;
import ru.cft.template.service.TransferService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("potato/api/users/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/hesoyam")
    public WalletShortResponse hesoyam(Authentication authentication, @RequestBody AmountBody body){
        return transferService.hesoyam(authentication, body);
    }

    @PostMapping("/casino")
    public WalletShortResponse casino(Authentication authentication, @RequestBody AmountBody body){
        return transferService.casino(authentication, body);
    }

    @PostMapping("/viaphone")
    public TransferResponse createTransferByPhone(
            Authentication authentication,
            @RequestBody TransferByPhoneBody body
    ){
        return transferService.createTransferByRecipientPhone(authentication, body);
    }

    @PostMapping("/viaid")
    public TransferResponse createTransferById(
            Authentication authentication,
            @RequestBody TransferByIdBody body
    ){
        return transferService.createTransferByRecipientId(authentication, body);
    }

    @PostMapping("/viainvoice")
    public TransferResponse createTransferByInvoice(
            Authentication authentication,
            @RequestBody TransferByInvoiceBody body
    ){
        return transferService.createTransferByInvoice(authentication, body);
    }

    @GetMapping("/getall")
    public List<TransferResponse> getAllTransfers(Authentication authentication){
        return transferService.getAllTransfers(authentication);
    }

    @GetMapping("/getviarecipientid/{id}")
    public List<TransferResponse> getAllTransfersByRecipientId(Authentication authentication, @PathVariable String id){
        return transferService.getAllTransfersByRecipientId(authentication, UUID.fromString(id));
    }

    @GetMapping("/getviamywallet")
    public List<TransferResponse> getAllTransfersBySenderWallet(Authentication authentication){
        return transferService.getAllTransfersBySenderWallet(authentication);
    }

    @GetMapping("/getviatype/{type}")
    public List<TransferResponse> getAllTransfersByType(Authentication authentication, @PathVariable String type){
        TransferType transferType = TransferType.valueOf(type.toUpperCase());
        return transferService.getAllTransfersByType(authentication, transferType);
    }
}

