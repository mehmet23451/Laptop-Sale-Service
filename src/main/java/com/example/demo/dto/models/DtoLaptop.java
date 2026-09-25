package com.example.demo.dto.models;

import com.example.demo.dto.DtoBase;
import com.example.demo.entity.enums.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class DtoLaptop extends DtoBase {
    private LaptopModel laptopModel;
    private RamOption ramOption;
    private StorageOption storageOption;
    private Color color;
    private Condition condition;
    private LaptopStatus laptopStatus;
    private Integer productionYear;
    private BigDecimal price;
}
