package com.example.demo.dto.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DtoAddressIU {
    @NotNull
    private String city;
    @NotNull
    private String district;
    @NotNull
    private String neighborhood;
    @NotNull
    private String street;
}
