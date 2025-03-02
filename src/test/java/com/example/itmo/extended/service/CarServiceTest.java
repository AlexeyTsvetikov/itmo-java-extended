package com.example.itmo.extended.service;

import com.example.itmo.extended.exception.CommonBackendException;
import com.example.itmo.extended.model.db.entity.Car;
import com.example.itmo.extended.model.db.entity.User;
import com.example.itmo.extended.model.db.repository.CarRepository;
import com.example.itmo.extended.model.dto.request.CarInfoRequest;
import com.example.itmo.extended.model.dto.response.CarInfoResponse;
import com.example.itmo.extended.model.enums.CarStatus;
import com.example.itmo.extended.utils.PaginationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private UserService userService;

    @Mock
    private CarRepository carRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void addCar() {
        CarInfoRequest request = new CarInfoRequest();
        request.setModel("X-Trail");

        Car car = new Car();
        car.setId(1l);
        car.setModel(request.getModel());

        when(carRepository.save(any(Car.class))).thenReturn(car);

        CarInfoResponse carInfoResponse = carService.addCar(request);
        assertEquals(car.getId(), carInfoResponse.getId());
    }

    @Test(expected = CommonBackendException.class)
    public void addCarExists() {
        CarInfoRequest request = new CarInfoRequest();
        request.setModel("X-Trail");
        request.setYear(2021);

        Car car = new Car();
        car.setId(1l);
        car.setModel(request.getModel());
        car.setYear(request.getYear());

        when(carRepository.findByModelAndYear(car.getModel(), car.getYear())).thenReturn(Optional.of(car));
        carService.addCar(request);
    }

    @Test
    public void getCar() {
        Car car = new Car();
        car.setId(1L);
        car.setModel("X-Trail");

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        CarInfoResponse carInfoResponse = carService.getCar(car.getId());
        assertEquals(car.getModel(), carInfoResponse.getModel());
    }

    @Test
    public void getCarFromDB() {
        Car car = new Car();
        car.setId(1L);
        car.setModel("X-Trail");

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        Car carFromDB = carService.getCarFromDB(car.getId());
        assertEquals(car.getModel(), carFromDB.getModel());

    }

    @Test(expected = CommonBackendException.class)
    public void getCarFromDBNotFound() {
        carService.getCarFromDB(1L);
    }

    @Test
    public void updateCar() {
        Car car = new Car();
        car.setId(1L);
        car.setModel("X-Trail");

        CarInfoRequest carInfoRequest = new CarInfoRequest();
        carInfoRequest.setModel("Qashqai");

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        carService.updateCar(car.getId(), carInfoRequest);
        assertEquals(carInfoRequest.getModel(), car.getModel());
    }

    @Test
    public void deleteCar() {
        Car car = new Car();
        car.setId(1L);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        carService.deleteCar(car.getId());
        verify(carRepository, times(1)).save(any(Car.class));
        assertEquals(CarStatus.DELETED, car.getStatus());
    }

    @Test
    public void getAllCarsWithFilter() {
        int pageNumber = 0;
        int pageSize = 10;
        String sortField = "name";
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String filter = "active";

        Pageable pageable = PaginationUtils.getPageRequest(pageNumber, pageSize, sortField, sortDirection);

        Car car = new Car();
        CarInfoResponse carInfoResponse = new CarInfoResponse();

        when(carRepository.findAllFiltered(pageable, filter)).thenReturn(new PageImpl<>(Arrays.asList(car)));
        when(mapper.convertValue(car, CarInfoResponse.class)).thenReturn(carInfoResponse);

        Page<CarInfoResponse> result = carService.getAllCars(pageNumber, pageSize, sortField, sortDirection, filter);

        assertEquals(1, result.getContent().size());
        assertEquals(carInfoResponse, result.getContent().get(0));
        assertEquals(1,result.getTotalElements());
    }

    @Test
    public void getAllCarsWithoutFilter() {
        int pageNumber = 0;
        int pageSize = 10;
        String sortField = "name";
        Sort.Direction sortDirection = Sort.Direction.ASC;

        Pageable pageable = PaginationUtils.getPageRequest(pageNumber, pageSize, sortField, sortDirection);

        Car car = new Car();
        CarInfoResponse carInfoResponse = new CarInfoResponse();

        when(carRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(car)));
        when(mapper.convertValue(car, CarInfoResponse.class)).thenReturn(carInfoResponse);

        Page<CarInfoResponse> result = carService.getAllCars(pageNumber, pageSize, sortField, sortDirection, null);

        assertEquals(1, result.getContent().size());
        assertEquals(carInfoResponse, result.getContent().get(0));
        assertEquals(1,result.getTotalElements());
    }

    @Test
    public void getAllCarsEmptyResults() {
        int pageNumber = 0;
        int pageSize = 10;
        String sortField = "name";
        Sort.Direction sortDirection = Sort.Direction.ASC;

        Pageable pageable = PaginationUtils.getPageRequest(pageNumber, pageSize, sortField, sortDirection);
        when(carRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList()));

        Page<CarInfoResponse> result = carService.getAllCars(pageNumber, pageSize, sortField, sortDirection, null);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0,result.getTotalElements());
    }

    @Test
    public void getUserCars() {
        Car car = new Car();
        car.setId(1L);
        car.setModel("X-Trail");
        List<Car> cars = List.of(car);

        User user = new User();
        user.setId(1L);

        when(userService.getUserFromDB(1L)).thenReturn(user);
        user.setCars(cars);
        List<CarInfoResponse> userCars = carService.getUserCars(user.getId());
        assertEquals(cars.get(0).getModel(), userCars.get(0).getModel());

    }

    @Test()
    public void linkCarAndDriverCarExists() {

    }
}