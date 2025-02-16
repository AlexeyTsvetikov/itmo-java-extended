package com.example.itmo.extended.model.db.repository;

import com.example.itmo.extended.model.db.entity.Car;
import com.example.itmo.extended.model.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Car getCarByModelAndYear(String model, Integer year);

    List<Car> findAllByStatus(CarStatus status);

    @Query(nativeQuery = true, value = "select * from cars where true order by cars.updated_at  limit 1")
    Car findByUpdateDate();

    @Query("select c from Car c where c.status <> :status")
    List<Car> findAllByStatusNot(@Param("status") String status);

    @Query("select u.cars from User u where u.id =:userId")
    List<Car> getUserCarsBrandNames(@Param("userId") Long userId);



}
