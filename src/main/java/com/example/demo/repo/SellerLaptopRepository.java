package com.example.demo.repo;

import com.example.demo.entity.Laptop;
import com.example.demo.entity.Seller;
import com.example.demo.entity.SellerLaptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerLaptopRepository extends JpaRepository<SellerLaptop,Long> {
    boolean existsBySellerAndLaptop(Seller seller, Laptop laptop);
}
