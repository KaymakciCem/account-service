package com.assignment.account.app.service;

import com.assignment.account.app.dto.AccountDto;
import com.assignment.account.app.dto.CreateAccountRequest;
import com.assignment.account.app.dto.TransactionDto;
import com.assignment.account.app.entity.Account;
import com.assignment.account.app.entity.Customer;
import com.assignment.account.app.exception.CustomerNotFoundException;
import com.assignment.account.app.feignclients.TransactionClient;
import com.assignment.account.app.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountCreationServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private TransactionClient transactionClient;

    @InjectMocks
    private AccountCreationService accountCreationService;

    @Test
    void openAccount_customer_not_exits() {
        CreateAccountRequest accountRequest = getAccountDto("500", BigDecimal.TEN);

        when(customerService.findCustomerBy("500")).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            accountCreationService.openAccount(accountRequest);
        });

        String expectedMessage = "Customer could not be found. Id: 500";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void openAccount_successful_no_initial_credit() {
        CreateAccountRequest accountRequest = getAccountDto("500", BigDecimal.ZERO);

        Customer customer = getCustomer("500", "testName", "testSurname");

        when(customerService.findCustomerBy("500")).thenReturn(Optional.of(customer));

        AccountDto result = accountCreationService.openAccount(accountRequest);

        assertThat(result.getAccountId()).isEqualTo("500");
        assertThat(result.getBalance()).isEqualTo(BigDecimal.ZERO);

        verify(customerService).save(customer);
        verify(transactionClient, never()).addTransaction(any());
    }

    @Test
    void openAccount_successful_with_initial_credit() {
        CreateAccountRequest accountRequest = getAccountDto("500", BigDecimal.TEN);

        Customer customer = getCustomer("500", "testName", "testSurname");

        when(customerService.findCustomerBy("500")).thenReturn(Optional.of(customer));

        AccountDto result = accountCreationService.openAccount(accountRequest);

        verify(transactionClient).addTransaction(TransactionDto.builder()
                .accountId("500")
                .amount(BigDecimal.TEN)
                .build());

        assertThat(result.getAccountId()).isEqualTo("500");
        assertThat(result.getBalance()).isEqualTo(BigDecimal.TEN);
        verify(customerService).save(customer);
    }

    private CreateAccountRequest getAccountDto(final String customerId, final BigDecimal initialCredit) {
        return CreateAccountRequest.builder()
                .customerId(customerId)
                .initialCredit(initialCredit)
                .build();
    }

    private Customer getCustomer(final String customerId, final String name, final String surname) {
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName(name);
        customer.setSurname(surname);

        return customer;
    }
}