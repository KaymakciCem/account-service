package com.assignment.account.app.service;

import com.assignment.account.app.dto.TransactionDto;
import com.assignment.account.app.feignclients.TransactionClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private final TransactionClient transactionClient;

    public List<TransactionDto> getTransactions(final String accountId) {
        return transactionClient.getTransactions(accountId);
    }
}
