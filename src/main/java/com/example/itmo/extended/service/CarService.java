package com.example.itmo.extended.service;

import com.example.itmo.extended.model.dto.request.CarInfoRequest;
import com.example.itmo.extended.model.dto.response.CarInfoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarService {

    CarInfoResponse getCar(Long id);

    CarInfoResponse addCar(CarInfoRequest request);

    CarInfoResponse updateCar(Long id, CarInfoRequest request);

    void deleteCar(Long id);

    List<CarInfoResponse> getAllCars();
}
