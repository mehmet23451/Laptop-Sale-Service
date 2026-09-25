package com.example.demo.dto.models;

import com.example.demo.dto.DtoBase;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Laptop;
import com.example.demo.entity.Seller;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DtoSoldLaptop extends DtoBase {
    private DtoSeller seller;
    private DtoLaptop laptop;
    private DtoCustomer customer;
}
