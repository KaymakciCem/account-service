package com.assignment.account.app.service;

import com.assignment.account.app.dto.AccountDto;
import com.assignment.account.app.dto.CreateAccountRequest;
import com.assignment.account.app.dto.TransactionDto;
import com.assignment.account.app.entity.Account;
import com.assignment.account.app.entity.Customer;
import com.assignment.account.app.exception.CustomerNotFoundException;
import com.assignment.account.app.feignclients.TransactionClient;
import com.assignment.account.app.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountCreationService {

    private final CustomerService customerService;
    private final TransactionClient transactionClient;

    public AccountDto openAccount(final CreateAccountRequest request) {
        Optional<Customer> customer = customerService.findCustomerBy(request.getCustomerId());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer could not be found. Id: " + request.getCustomerId());
        }

        if (request.getInitialCredit().compareTo(BigDecimal.ZERO) > 0) {
            transactionClient.addTransaction(TransactionDto.builder()
                    .accountId(request.getCustomerId())
                    .amount(request.getInitialCredit())
                    .build());
        }

        final Account account = new Account();
        account.setBalance(request.getInitialCredit());
        customer.get().getAccounts().add(account);

        customerService.save(customer.get());

        return AccountDto.builder()
                .accountId(customer.get().getId())
                .balance(request.getInitialCredit())
                .build();
    }
}
