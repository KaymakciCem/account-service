package com.assignment.account.app.controller;

import com.assignment.account.app.dto.CustomerDto;
import com.assignment.account.app.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(value = "/info/{customerId}")
    public ResponseEntity<CustomerDto> retrieveUserInfo(@PathVariable String customerId) {
        CustomerDto customerDto = customerService.retrieveCustomerInfo(customerId);
        return ResponseEntity.ok(customerDto);
    }

}
