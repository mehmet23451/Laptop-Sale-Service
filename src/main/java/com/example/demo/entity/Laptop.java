package com.example.demo.entity;

import com.example.demo.entity.enums.*;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "laptop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Laptop extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private LaptopModel laptopModel;
    @Enumerated(EnumType.STRING)
    private RamOption ramOption;
    @Enumerated(EnumType.STRING)
    private StorageOption storageOption;
    @Enumerated(EnumType.STRING)
    private Color color;
    @Enumerated(EnumType.STRING)
    private Condition condition;
    @Enumerated(EnumType.STRING)
    private LaptopStatus laptopStatus;
    private Integer productionYear;
    private BigDecimal price;

}
