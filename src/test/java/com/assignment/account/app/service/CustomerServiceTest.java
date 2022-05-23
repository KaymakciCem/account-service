package com.assignment.account.app.service;

import com.assignment.account.app.dto.CustomerDto;
import com.assignment.account.app.dto.TransactionDto;
import com.assignment.account.app.entity.Customer;
import com.assignment.account.app.exception.CustomerNotFoundException;
import com.assignment.account.app.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private static final String ACCOUNT_ID = "123";

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void retrieveCustomerInfo_customer_not_found() {
        when(customerService.findCustomerBy(ACCOUNT_ID)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.retrieveCustomerInfo(ACCOUNT_ID);
        });

        String expectedMessage = "Customer could not be found. Id: 123";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void retrieveCustomerInfo_customer_found_no_transactions() {
        Customer customer = getCustomer("500", "testName", "testSurname");
        when(customerService.findCustomerBy(ACCOUNT_ID)).thenReturn(Optional.of(customer));

        when(accountService.getTransactions(ACCOUNT_ID)).thenReturn(Collections.emptyList());

        CustomerDto result = customerService.retrieveCustomerInfo(ACCOUNT_ID);

        assertThat(result.getName()).isEqualTo("testName");
        assertThat(result.getSurname()).isEqualTo("testSurname");
        assertThat(result.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getTransactions()).isEmpty();
    }

    @Test
    void retrieveCustomerInfo_customer_found_with_transactions() {
        Customer customer = getCustomer("500", "testName", "testSurname");
        when(customerService.findCustomerBy(ACCOUNT_ID)).thenReturn(Optional.of(customer));

        TransactionDto trx1 = getTransaction(BigDecimal.valueOf(100));
        TransactionDto trx2 = getTransaction(BigDecimal.valueOf(200));
        TransactionDto trx3 = getTransaction(BigDecimal.valueOf(300));

        when(accountService.getTransactions(ACCOUNT_ID)).thenReturn(List.of(trx1, trx2, trx3));

        CustomerDto result = customerService.retrieveCustomerInfo(ACCOUNT_ID);

        assertThat(result.getName()).isEqualTo("testName");
        assertThat(result.getSurname()).isEqualTo("testSurname");
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(600));
        assertThat(result.getTransactions()).containsExactlyElementsOf(List.of(trx1, trx2, trx3));
    }

    private TransactionDto getTransaction(final BigDecimal amount) {
        return TransactionDto.builder()
                .amount(amount)
                .accountId(ACCOUNT_ID)
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