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

    @GetMapping("/getallincoming")
    public List<InvoiceResponse> getAllIncomingInvoices(Authentication auth){
        return invoiceService.getAllIncomingInvoices(auth);
    }

    @GetMapping("/getalloutgoing")
    public List<InvoiceResponse> getAllOutGoingInvoices(Authentication auth){
        return invoiceService.getAllOutgoingInvoices(auth);
    }

    @GetMapping("/{id}")
    public InvoiceResponse getInvoice(Authentication authentication, @PathVariable UUID id){
        return invoiceService.getInvoice(authentication, id);
    }

    @GetMapping("/last")
    public InvoiceResponse getLastInvoice(Authentication auth){
        return invoiceService.getLastIncomingInvoice(auth);
    }

    @GetMapping("/first")
    public InvoiceResponse getFirstInvoice(Authentication auth){
        return invoiceService.getFirstIncomingInvoice(auth);
    }

    @GetMapping("/gettotal")
    public Long getTotalInvoice(Authentication auth){
        return invoiceService.getTotalInvoices(auth);
    }
}
