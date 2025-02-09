package com.example.itmo.extended.service.impl;

import com.example.itmo.extended.model.dto.request.CarInfoRequest;
import com.example.itmo.extended.model.dto.response.CarInfoResponse;
import com.example.itmo.extended.model.enums.CarType;
import com.example.itmo.extended.model.enums.Color;
import com.example.itmo.extended.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final ObjectMapper mapper;

    @Override
    public CarInfoResponse getCar(Long id) {
        return CarInfoResponse.builder()
                .id(1L)
                .brand("Nissan")
                .model("X-Trail")
                .color(Color.WHITE)
                .year(2021)
                .price(3_500_000L)
                .isNew(false)
                .type(CarType.SUV)
                .build();
    }

    @Override
    public CarInfoResponse addCar(CarInfoRequest request) {
        CarInfoResponse carInfoResponse = mapper.convertValue(request, CarInfoResponse.class);
        carInfoResponse.setId(1L);
        return carInfoResponse;
    }

    @Override
    public CarInfoResponse updateCar(Long id, CarInfoRequest request) {
        if (id != 1L) {
            log.error("Car with id {} not found", id);
            return null;
        }
        return CarInfoResponse.builder()
                .id(1L)
                .brand("Nissan")
                .model("Qashqai")
                .color(Color.BLACK)
                .year(2022)
                .price(4_500_000L)
                .isNew(false)
                .type(CarType.SUV)
                .build();
    }

    @Override
    public void deleteCar(Long id) {
        if (id != 1L) {
            log.error("Car with id {} not found", id);
            return;
        }
        log.info("Car with id {} deleted", id);
    }

    @Override
    public List<CarInfoResponse> getAllCars() {
        return List.of(CarInfoResponse.builder()
                .id(1L)
                .brand("Nissan")
                .model("Qashqai")
                .color(Color.BLACK)
                .year(2022)
                .price(4_500_000L)
                .isNew(false)
                .type(CarType.SUV)
                .build());
    }

    @Override
    public CarInfoResponse getCar(String brand, String model) {
        return getCar(1L);
    }
}
