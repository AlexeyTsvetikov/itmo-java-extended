package com.example.itmo.extended.model.db.repository;


import com.example.itmo.extended.model.db.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("select c from Car c where c.brand like %:filter% or c.model like %:filter%")
    Page<Car> findAllFiltered(Pageable pageRequest, @Param("filter") String filter);

    Optional<Car> findByModelAndYear (String model, Integer year);
}
