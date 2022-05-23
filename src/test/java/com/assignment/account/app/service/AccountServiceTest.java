package com.assignment.account.app.service;

import com.assignment.account.app.dto.TransactionDto;
import com.assignment.account.app.feignclients.TransactionClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final String ACCOUNT_ID = "123";

    @Mock
    private TransactionClient transactionClient;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getTransactions() {
        when(transactionClient.getTransactions(ACCOUNT_ID)).thenReturn(getCustomerTransactions());

        List<TransactionDto> transactions = accountService.getTransactions(ACCOUNT_ID);
        assertThat(transactions.size()).isEqualTo(1);
        assertThat(transactions.get(0).getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(transactions.get(0).getAccountId()).isEqualTo("123");

    }

    private List<TransactionDto> getCustomerTransactions() {
        return Collections.singletonList(new TransactionDto(ACCOUNT_ID, BigDecimal.TEN));

    }
}