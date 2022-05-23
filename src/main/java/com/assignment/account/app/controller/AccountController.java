package com.assignment.account.app.controller;

import com.assignment.account.app.dto.AccountDto;
import com.assignment.account.app.dto.CreateAccountRequest;
import com.assignment.account.app.service.AccountCreationService;
import com.assignment.account.app.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountCreationService accountCreationService;

    @PostMapping(value = "/open")
    public ResponseEntity<AccountDto> openAccount(@RequestBody CreateAccountRequest request) {
        AccountDto accountDto = accountCreationService.openAccount(request);
        return ResponseEntity.ok(accountDto);
    }
}
