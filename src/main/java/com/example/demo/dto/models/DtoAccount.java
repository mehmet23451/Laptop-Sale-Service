package com.example.demo.dto.models;

import com.example.demo.dto.DtoBase;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DtoAccount extends DtoBase {
    private String accountNo;

    private String iban;

    private BigDecimal amount;
}
