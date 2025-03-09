package com.github.bilalr.bankapi.service;

import com.github.bilalr.bankapi.exception.AccountNotFoundException;
import com.github.bilalr.bankapi.exception.InsufficientFundsException;
import com.github.bilalr.bankapi.exception.InvalidRequestException;
import com.github.bilalr.bankapi.model.Account;
import com.github.bilalr.bankapi.model.Transfer;
import com.github.bilalr.bankapi.repository.AccountRepository;
import com.github.bilalr.bankapi.repository.TransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {
    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void testCreateTransferOK() {
        Account account1 = new Account(1, 1, BigDecimal.TEN, "EUR");
        Account account2 = new Account(2, 2, BigDecimal.ZERO, "EUR");
        Transfer transfer = new Transfer(null, 1, 1, 2, 2,
                BigDecimal.TEN);

        TransferService service = new TransferService(accountRepository, transferRepository);
        when(accountRepository.getAccountById(1, 1)).thenReturn(Optional.of(account1));
        when(accountRepository.getAccountById(2, 2)).thenReturn(Optional.of(account2));

        service.createTransfer(transfer);

        Mockito.verify(transferRepository, times(1)).createTransfer(transfer);
        Mockito.verify(accountRepository, times(1)).updateBalance(account1, BigDecimal.ZERO);
        Mockito.verify(accountRepository, times(1)).updateBalance(account2, BigDecimal.TEN);
    }

    @Test
    void testCreateTransferAccountNotFound() {
        Account account = new Account(1, 1, BigDecimal.TEN, "EUR");
        Transfer transfer = new Transfer(null, 1, 1, 2, 2,
                BigDecimal.TEN);

        TransferService service = new TransferService(accountRepository, transferRepository);
        when(accountRepository.getAccountById(1, 1)).thenReturn(Optional.empty());
        when(accountRepository.getAccountById(2, 2)).thenReturn(Optional.of(account));

        assertThrows(AccountNotFoundException.class, () -> service.createTransfer(transfer));

        Mockito.verify(transferRepository, times(0)).createTransfer(any());
        Mockito.verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    void testCreateTransferInsufficientFunds() {
        Account account1 = new Account(1, 1, BigDecimal.TEN, "EUR");
        Account account2 = new Account(2, 2, BigDecimal.ZERO, "EUR");
        Transfer transfer = new Transfer(null, 1, 1, 2, 2,
                new BigDecimal("11"));

        TransferService service = new TransferService(accountRepository, transferRepository);
        when(accountRepository.getAccountById(1, 1)).thenReturn(Optional.of(account1));
        when(accountRepository.getAccountById(2, 2)).thenReturn(Optional.of(account2));

        assertThrows(InsufficientFundsException.class, () -> service.createTransfer(transfer));

        Mockito.verify(transferRepository, times(0)).createTransfer(any());
        Mockito.verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    void testCreateTransferDifferentCurrencies() {
        Account account1 = new Account(1, 1, BigDecimal.TEN, "EUR");
        Account account2 = new Account(2, 2, BigDecimal.ZERO, "GBP");
        Transfer transfer = new Transfer(null, 1, 1, 2, 2,
                BigDecimal.TEN);

        TransferService service = new TransferService(accountRepository, transferRepository);
        when(accountRepository.getAccountById(1, 1)).thenReturn(Optional.of(account1));
        when(accountRepository.getAccountById(2, 2)).thenReturn(Optional.of(account2));

        assertThrows(InvalidRequestException.class, () -> service.createTransfer(transfer));

        Mockito.verify(transferRepository, times(0)).createTransfer(any());
        Mockito.verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    void testCreateTransferSameAccount() {
        Transfer transfer = new Transfer(null, 1, 1, 1, 1,
                BigDecimal.TEN);

        TransferService service = new TransferService(accountRepository, transferRepository);

        assertThrows(InvalidRequestException.class, () -> service.createTransfer(transfer));

        Mockito.verify(accountRepository, times(0)).getAccountById(any(), any());
        Mockito.verify(transferRepository, times(0)).createTransfer(any());
        Mockito.verify(accountRepository, times(0)).updateBalance(any(), any());
    }
}
