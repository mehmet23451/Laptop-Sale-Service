package com.example.demo.dto.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DtoSellerIU {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Long addressId;
    @NotNull
    private Long accountId;
}
