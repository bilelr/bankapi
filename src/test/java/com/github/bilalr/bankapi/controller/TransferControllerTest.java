package com.github.bilalr.bankapi.controller;

import com.github.bilalr.bankapi.exception.InvalidRequestException;
import com.github.bilalr.bankapi.model.Transfer;
import com.github.bilalr.bankapi.service.TransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransferService transferService;

    @Test
    void testCreateTransfer() throws Exception {
        Transfer transfer = new Transfer(null, 1, 1, 2, 3,
                new BigDecimal("322.24"));

        this.mockMvc.perform(post("/api/transfers")
                        .content("""
                                {
                                  "fromCustomerId": 1,
                                  "fromAccountId": 1,
                                  "toCustomerId": 2,
                                  "toAccountId": 3,
                                  "amount": 322.24
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Mockito.verify(transferService, times(1)).createTransfer(transfer);
    }

    @Test
    void testFailedCreateTransfer() throws Exception {
        Transfer transfer = new Transfer(null, 1, 1, 2, 3,
                BigDecimal.ZERO);

        doThrow(new InvalidRequestException()).when(transferService).createTransfer(transfer);

        this.mockMvc.perform(post("/api/transfers")
                        .content("""
                                {
                                  "fromCustomerId": 1,
                                  "fromAccountId": 1,
                                  "toCustomerId": 2,
                                  "toAccountId": 3,
                                  "amount": 0
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTransfersForAccount() throws Exception {
        List<Transfer> transfers = List.of(
                new Transfer(1, 1, 1, 2, 1, BigDecimal.ONE),
                new Transfer(2, 2, 2, 1, 1, BigDecimal.TEN),
                new Transfer(3, 3, 3, 1, 1, BigDecimal.ZERO)
        );

        when(transferService.getTransfersForAccount(1, 1)).thenReturn(transfers);

        this.mockMvc.perform(get("/api/transfers").queryParam("customerId", "1")
                        .queryParam("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                  {"id":1,"fromCustomerId":1,"fromAccountId":1,"toCustomerId":2,"toAccountId":1,"amount":1},
                                  {"id":2,"fromCustomerId":2,"fromAccountId":2,"toCustomerId":1,"toAccountId":1,"amount":10},
                                  {"id":3,"fromCustomerId":3,"fromAccountId":3,"toCustomerId":1,"toAccountId":1,"amount":0}
                                ]
                                """));
    }
}
