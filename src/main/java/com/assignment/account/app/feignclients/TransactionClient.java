package com.assignment.account.app.feignclients;

import com.assignment.account.app.dto.TransactionDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(url = "${transaction.service.url}", path = "/transaction", value = "transaction-client")
public interface TransactionClient {
    @GetMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TransactionDto> getTransactions(@PathVariable("accountId") String accountId);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void addTransaction(@RequestBody TransactionDto transactionDto);
}
