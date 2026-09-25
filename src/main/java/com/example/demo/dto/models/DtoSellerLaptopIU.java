package com.example.demo.dto.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DtoSellerLaptopIU {
    @NotNull
    private Long sellerId;
    @NotNull
    private Long laptopId;
}
