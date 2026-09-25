package com.example.demo.dto.models;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Laptop;
import com.example.demo.entity.Seller;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DtoSoldLaptopIU {
    @NotNull
    private Long sellerId;
    private Long laptopId;
    private Long customerId;
}
