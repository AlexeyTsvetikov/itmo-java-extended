package com.example.itmo.extended.service;

import com.example.itmo.extended.model.db.entity.Car;
import com.example.itmo.extended.model.db.entity.User;
import com.example.itmo.extended.model.db.repository.CarRepository;
import com.example.itmo.extended.model.dto.request.CarInfoRequest;
import com.example.itmo.extended.model.dto.response.CarInfoResponse;
import com.example.itmo.extended.model.dto.response.UserInfoResponse;
import com.example.itmo.extended.model.enums.CarStatus;
import com.example.itmo.extended.utils.PaginationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {
    private final ObjectMapper mapper;
    private final UserService userService;
    private final CarRepository carRepository;

    public CarInfoResponse addCar(CarInfoRequest request) {
        Car car = mapper.convertValue(request, Car.class);
        car.setStatus(CarStatus.CREATED);

        Car save = carRepository.save(car);
        return mapper.convertValue(save, CarInfoResponse.class);
    }

    public CarInfoResponse getCar(Long id) {
        Car car = getCarFromDB(id);
        return mapper.convertValue(car, CarInfoResponse.class);
    }

    public Car getCarFromDB(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.orElse(new Car());
    }


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

    public void deleteCar(Long id) {
        Car carFromDB = getCarFromDB(id);
        if (carFromDB.getId() == null) {
            log.error("Car with id {} not found", id);
            return;
        }
        carFromDB.setStatus(CarStatus.DELETED);
        carRepository.save(carFromDB);
    }

    public Page<CarInfoResponse> getAllCars(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {

        Pageable pageRequest = PaginationUtils.getPageRequest(page, perPage, sort, order);
        Page<Car> cars;

        if (StringUtils.hasText(filter)) {
            cars = carRepository.findAllFiltered(pageRequest, filter);
        } else {
            cars = carRepository.findAll(pageRequest);
        }

        List<CarInfoResponse> content = cars.getContent().stream()
                .map(c -> mapper.convertValue(c, CarInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, cars.getTotalElements());
    }


    public List<CarInfoResponse> getUserCars(Long userId) {
        User user = userService.getUserFromDB(userId);
        if (user.getId() == null) {
            log.error("User with id {} not found", userId);
            return List.of();
        }
        List<Car> userCars = user.getCars();
        return userCars.stream()
                .map(car -> mapper.convertValue(car, CarInfoResponse.class))
                .collect(Collectors.toList());
    }

    public CarInfoResponse linkCarAndDriver(Long carId, Long userId) {
        Car carFromDB = getCarFromDB(carId);
        User userFromDB = userService.getUserFromDB(userId);

        if (carFromDB.getId() == null || userFromDB.getId() == null) {
            return CarInfoResponse.builder().build();
        }

        List<Car> cars = userFromDB.getCars();

        Car existingCar = cars.stream().filter(car -> car.getId().equals(carId)).findFirst().orElse(null);

        if (existingCar != null) {
            return mapper.convertValue(existingCar, CarInfoResponse.class);
        }

        cars.add(carFromDB);
        User user = userService.updateCarList(userFromDB);

        carFromDB.setUser(user);
        carRepository.save(carFromDB);

        CarInfoResponse carInfoResponse = mapper.convertValue(carFromDB, CarInfoResponse.class);
        UserInfoResponse userInfoResponse = mapper.convertValue(user, UserInfoResponse.class);

        carInfoResponse.setUser(userInfoResponse);

        return carInfoResponse;
    }
}
