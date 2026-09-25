package com.example.demo.service;

import com.example.demo.dto.models.DtoSoldLaptop;
import com.example.demo.dto.models.DtoSoldLaptopIU;

public interface LaptopSaleService {
    public DtoSoldLaptop sellLaptop(DtoSoldLaptopIU dtoSoldLaptopIU);
}
