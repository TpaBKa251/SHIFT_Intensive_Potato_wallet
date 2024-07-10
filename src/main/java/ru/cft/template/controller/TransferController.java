package ru.cft.template.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.enums.TransferType;
import ru.cft.template.model.request.AmountBody;
import ru.cft.template.model.request.TransferByIdBody;
import ru.cft.template.model.request.TransferByInvoiceBody;
import ru.cft.template.model.request.TransferByPhoneBody;
import ru.cft.template.model.response.TransactionHistoryResponse;
import ru.cft.template.model.response.TransferResponse;
import ru.cft.template.model.response.WalletShortResponse;
import ru.cft.template.service.TransferService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("potato/api/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/hesoyam")
    public WalletShortResponse hesoyam(Authentication authentication, @RequestBody @Valid AmountBody body){
        return transferService.hesoyam(authentication, body);
    }

    @PostMapping("/casino")
    public WalletShortResponse casino(Authentication authentication, @RequestBody @Valid AmountBody body){
        return transferService.casino(authentication, body);
    }

    @PostMapping("/roulette/{min}/{max}/{number}")
    public WalletShortResponse casino(Authentication authentication, @RequestBody @Valid AmountBody body, @PathVariable long min, @PathVariable long max, @PathVariable long number){
        return transferService.roulette(authentication, body, number, min, max);
    }

    @PostMapping("/viaphone")
    public TransferResponse createTransferByPhone(
            Authentication authentication,
            @RequestBody @Valid TransferByPhoneBody body
    ){
        return transferService.createTransferByRecipientPhone(authentication, body);
    }

    @PostMapping("/viaid")
    public TransferResponse createTransferById(
            Authentication authentication,
            @RequestBody @Valid TransferByIdBody body
    ){
        return transferService.createTransferByRecipientId(authentication, body);
    }

    @PostMapping("/viainvoice")
    public TransferResponse createTransferByInvoice(
            Authentication authentication,
            @RequestBody @Valid TransferByInvoiceBody body
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

    @GetMapping("/getHistory/{type}")
    public List<TransactionHistoryResponse> getHistory(Authentication authentication, @PathVariable String type){
        type = type.toUpperCase();
        return transferService.getHistory(authentication, TransferType.valueOf(type));
    }
}

