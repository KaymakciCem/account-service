package com.assignment.account.app.service;

import com.assignment.account.app.dto.CustomerDto;
import com.assignment.account.app.dto.TransactionDto;
import com.assignment.account.app.entity.Customer;
import com.assignment.account.app.exception.CustomerNotFoundException;
import com.assignment.account.app.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    public Optional<Customer> findCustomerBy(final String id) {
        return customerRepository.findById(id);
    }

    public void save(final Customer customer) {
        customerRepository.save(customer);
    }

    public CustomerDto retrieveCustomerInfo(final String customerId) {
        final Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer could not be found. Id: " + customerId);
        }

        final List<TransactionDto> transactions = accountService.getTransactions(customerId);

        return CustomerDto.builder()
                .transactions(transactions)
                .balance(transactions.stream()
                        .map(TransactionDto::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .name(customer.get().getName())
                .surname(customer.get().getSurname())
                .build();
    }
}
