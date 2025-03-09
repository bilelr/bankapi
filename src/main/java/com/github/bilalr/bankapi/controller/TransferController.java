package com.github.bilalr.bankapi.controller;

import com.github.bilalr.bankapi.model.Transfer;
import com.github.bilalr.bankapi.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class exposes the REST end-points for the Transfer resource.
 * When a transfer is created, it moves funds from one account to another.
 * The two accounts (FROM account and TO account) can belong to different customers.
 */
@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createTransfer(@Valid @RequestBody Transfer transfer) {
        transferService.createTransfer(transfer);
    }

    @GetMapping
    public List<Transfer> getTransfersForAccount(@RequestParam Integer customerId,
                                                 @RequestParam Integer accountId) {
        return transferService.getTransfersForAccount(customerId, accountId);
    }
}
