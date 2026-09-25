package com.example.demo.repo;

import com.example.demo.entity.SoldLaptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldLaptopRepository extends JpaRepository<SoldLaptop,Long> {
}
