package com.example.demo.entity;

import com.example.demo.entity.enums.LaptopStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sold_laptop",uniqueConstraints = {@UniqueConstraint(columnNames = {"laptop_id"}, name = "uq_sold_laptop")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SoldLaptop extends BaseEntity{
    @ManyToOne
    private Seller seller;
    @ManyToOne
    private Laptop laptop;
    @ManyToOne
    private Customer customer;
}
