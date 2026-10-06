package com.example.demo.entity;

import com.example.demo.entity.enums.LaptopStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sold_laptop",uniqueConstraints = {@UniqueConstraint(columnNames = {"laptop_id"}, name = "uq_sold_laptop")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SoldLaptop extends BaseEntity{
    @ManyToOne
    private Seller seller;
    @ManyToOne
    private Laptop laptop;
    @ManyToOne
    private Customer customer;
}
