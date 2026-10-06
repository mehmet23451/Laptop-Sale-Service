package com.example.demo.dto.models;

import com.example.demo.dto.DtoBase;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DtoSeller extends DtoBase {
    private String firstName;
    private String lastName;
    private DtoAddress address;
    private DtoAccount account;
}
