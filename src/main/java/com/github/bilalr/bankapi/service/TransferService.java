package com.github.bilalr.bankapi.service;

import com.github.bilalr.bankapi.exception.AccountNotFoundException;
import com.github.bilalr.bankapi.exception.InsufficientFundsException;
import com.github.bilalr.bankapi.exception.InvalidRequestException;
import com.github.bilalr.bankapi.model.Account;
import com.github.bilalr.bankapi.model.Transfer;
import com.github.bilalr.bankapi.repository.AccountRepository;
import com.github.bilalr.bankapi.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * The transfer service applies the transaction to move the funds from one account to another as specified
 * in the input Transfer object.
 * Validation is done to ensure.
 * <li>Transfer is not to the same account</li>
 * <li>Both FROM and TO accounts exist</li>
 * <li>There are sufficient funds in the FROM account</li>
 */
@Service
public class TransferService {
    private final Logger logger = LoggerFactory.getLogger(TransferService.class);
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferService(AccountRepository accountRepository,
                           TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public void createTransfer(Transfer transfer) {
        validateTransfer(transfer);

        Optional<Account> fromAccountOpt = accountRepository.getAccountById(transfer.fromCustomerId(),
                transfer.fromAccountId());
        Optional<Account> toAccountOpt = accountRepository.getAccountById(transfer.toCustomerId(),
                transfer.toAccountId());

        if (fromAccountOpt.isEmpty() || toAccountOpt.isEmpty()) {
            throw new AccountNotFoundException();
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        validateTransfer(transfer, fromAccount, toAccount);
        performTransfer(transfer, fromAccount, toAccount);
    }

    private void validateTransfer(Transfer transfer) {
        if (transfer.fromAccountId().equals(transfer.toAccountId())) {
            throw new InvalidRequestException();
        }
    }

    private void validateTransfer(Transfer transfer, Account fromAccount, Account toAccount) {
        // Currency fromAccount and toAccount must be the same
        if (!fromAccount.currency().equals(toAccount.currency())) {
            throw new InvalidRequestException();
        }

        if (transfer.amount().compareTo(fromAccount.balance()) > 0) {
            throw new InsufficientFundsException();
        }
    }

    private void performTransfer(Transfer transfer, Account fromAccount, Account toAccount) {
        BigDecimal fromNewAmount = fromAccount.balance().subtract(transfer.amount());
        BigDecimal toNewAmount = toAccount.balance().add(transfer.amount());

        accountRepository.updateBalance(fromAccount, fromNewAmount);
        accountRepository.updateBalance(toAccount, toNewAmount);

        transferRepository.createTransfer(transfer);
    }

    public List<Transfer> getTransfersForAccount(Integer customerId, Integer accountId) {
        return transferRepository.getTransfersForAccount(customerId, accountId);
    }
}
