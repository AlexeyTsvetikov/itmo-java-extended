package com.example.itmo.extended.model.db.repository;

import com.example.itmo.extended.model.db.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Car getCarByModelAndYear(String model, Integer year);
}
