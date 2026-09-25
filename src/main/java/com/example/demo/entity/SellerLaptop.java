package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "seller_laptop",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"seller_id", "laptop_id"}, name = "uq_seller_laptop")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerLaptop extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "laptop_id", nullable = false)
    private Laptop laptop;
}