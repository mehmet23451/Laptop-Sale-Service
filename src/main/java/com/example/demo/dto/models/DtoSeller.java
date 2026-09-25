package com.example.demo.dto.models;

import com.example.demo.dto.DtoBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoSeller extends DtoBase {
    private String firstName;
    private String lastName;
    private DtoAddress adress;
    private DtoAccount account;
}
