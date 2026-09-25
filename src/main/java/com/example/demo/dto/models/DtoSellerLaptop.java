package com.example.demo.dto.models;

import com.example.demo.dto.DtoBase;
import com.example.demo.entity.Laptop;
import com.example.demo.entity.Seller;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DtoSellerLaptop extends DtoBase {

    private DtoSeller seller;

    private DtoLaptop laptop;
}
