package ru.cft.template.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.request.CreateInvoice;
import ru.cft.template.model.response.InvoiceResponse;
import ru.cft.template.service.InvoiceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("potato/api/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public InvoiceResponse addInvoice(@RequestBody @Valid CreateInvoice body, Authentication auth) {
        return invoiceService.createInvoice(auth, body);
    }

    @GetMapping("/getall")
    public List<InvoiceResponse> getAllInvoices(Authentication auth) {
        return invoiceService.getAllInvoices(auth);
    }

    @PatchMapping("/cancel/{id}")
    public InvoiceResponse cancelInvoice(Authentication authentication, @PathVariable UUID id){
        return invoiceService.cancelInvoice(authentication, id);
    }
}
