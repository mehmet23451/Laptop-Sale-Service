package com.example.demo.dto.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class DtoAccountIU {
    @NotNull
    private String accountNo;
    @NotNull
    private String iban;
    @NotNull
    private BigDecimal amount;
}
