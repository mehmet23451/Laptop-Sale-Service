package com.example.demo.dto.models;

import com.example.demo.entity.enums.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class DtoLaptopIU {
    @NotNull
    private LaptopModel laptopModel;
    @NotNull
    private RamOption ramOption;
    @NotNull
    private StorageOption storageOption;
    @NotNull
    private Color color;
    @NotNull
    private Condition condition;
    @NotNull
    private LaptopStatus laptopStatus;
    @NotNull
    private Integer productionYear;
    @NotNull
    private BigDecimal price;
}
