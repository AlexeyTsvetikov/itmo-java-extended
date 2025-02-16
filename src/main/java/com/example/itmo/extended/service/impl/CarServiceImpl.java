package com.example.itmo.extended.service.impl;

import com.example.itmo.extended.model.db.entity.Car;
import com.example.itmo.extended.model.db.repository.CarRepository;
import com.example.itmo.extended.model.dto.request.CarInfoRequest;
import com.example.itmo.extended.model.dto.response.CarInfoResponse;
import com.example.itmo.extended.model.enums.CarStatus;
import com.example.itmo.extended.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final ObjectMapper mapper;
    private final CarRepository carRepository;

    @Override
    public CarInfoResponse addCar(CarInfoRequest request) {
        Car car = mapper.convertValue(request, Car.class);
        car.setStatus(CarStatus.CREATED);

        Car save = carRepository.save(car);
        return mapper.convertValue(save, CarInfoResponse.class);
    }

    @Override
    public CarInfoResponse getCar(Long id) {
        Car car = getCarFromDB(id);
        return mapper.convertValue(car, CarInfoResponse.class);
    }

    public Car getCarFromDB(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.orElse(new Car());
    }


    @Override
    public CarInfoResponse updateCar(Long id, CarInfoRequest request) {
        Car carFromDB = getCarFromDB(id);
        if (carFromDB.getId() == null) {
            return mapper.convertValue(carFromDB, CarInfoResponse.class);
        }

        Car carReq = mapper.convertValue(request, Car.class);

        carFromDB.setBrand(carReq.getBrand() == null ? carFromDB.getBrand() : carReq.getBrand());
        carFromDB.setModel(carReq.getModel() == null ? carFromDB.getModel() : carReq.getModel());
        carFromDB.setColor(carReq.getColor() == null ? carFromDB.getColor() : carReq.getColor());
        carFromDB.setYear(carReq.getYear() == null ? carFromDB.getYear() : carReq.getYear());
        carFromDB.setPrice(carReq.getPrice() == null ? carFromDB.getPrice() : carReq.getPrice());
        carFromDB.setIsNew(carReq.getIsNew() == null ? carFromDB.getIsNew() : carReq.getIsNew());
        carFromDB.setType(carReq.getType() == null ? carFromDB.getType() : carReq.getType());

        carFromDB = carRepository.save(carFromDB);
        return mapper.convertValue(carFromDB, CarInfoResponse.class);
    }

    @Override
    public void deleteCar(Long id) {
        Car carFromDB = getCarFromDB(id);
        if (carFromDB.getId() == null) {
            log.error("Car with id {} not found", id);
            return;
        }
        carFromDB.setStatus(CarStatus.DELETED);
        carRepository.save(carFromDB);
    }

    @Override
    public List<CarInfoResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(car -> mapper.convertValue(car, CarInfoResponse.class))
                .collect(Collectors.toList());
    }
}
