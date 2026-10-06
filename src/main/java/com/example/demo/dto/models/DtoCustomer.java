package com.example.demo.dto.models;

import com.example.demo.dto.DtoBase;
import com.example.demo.entity.Account;
import com.example.demo.entity.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder

public class DtoCustomer extends DtoBase {
    private String firstName;
    private String lastName;
    private DtoAddress address;
    private DtoAccount account;
}
