package com.assignment.account.app.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Data
@Builder
public class CreateAccountRequest {

    @NotBlank
    private String customerId;

    @Min(value = 0)
    private BigDecimal initialCredit;
}

