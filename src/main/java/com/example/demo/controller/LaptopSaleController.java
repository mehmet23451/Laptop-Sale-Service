package com.example.demo.controller;

import com.example.demo.dto.models.DtoSoldLaptop;
import com.example.demo.dto.models.DtoSoldLaptopIU;

public interface LaptopSaleController {
    public RootEntity<DtoSoldLaptop> sellLaptop(DtoSoldLaptopIU dtoSoldLaptopIU);
}
