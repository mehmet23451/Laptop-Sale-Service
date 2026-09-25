package com.example.demo.controller.impl;

import com.example.demo.controller.LaptopSaleController;
import com.example.demo.controller.RestBaseController;
import com.example.demo.controller.RootEntity;
import com.example.demo.dto.models.DtoSoldLaptop;
import com.example.demo.dto.models.DtoSoldLaptopIU;
import com.example.demo.service.LaptopSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/rest")
public class LaptopSaleControllerImpl extends RestBaseController implements LaptopSaleController {
    @Autowired
    private LaptopSaleService laptopSaleService;
    @Override
    @PostMapping("/sellLaptop")
    public RootEntity<DtoSoldLaptop> sellLaptop(@RequestBody DtoSoldLaptopIU dtoSoldLaptopIU) {
        return ok(laptopSaleService.sellLaptop(dtoSoldLaptopIU));
    }
}
