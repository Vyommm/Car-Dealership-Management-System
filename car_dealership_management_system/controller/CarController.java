package com.cardealership.managementsystem.controller;

import com.cardealership.managementsystem.model.Car;
import com.cardealership.managementsystem.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car savedCar = carService.saveCar(car);
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        Car updatedCar = carService.updateCar(id, car);
        return new ResponseEntity<>(updatedCar, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/sold")
    public ResponseEntity<Car> markCarAsSold(@PathVariable Long id) {
        Car car = carService.markCarAsSold(id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars() {
        List<Car> availableCars = carService.getAvailableCars();
        return new ResponseEntity<>(availableCars, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Car>> searchCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer year) {
        List<Car> cars = carService.searchCars(make, model, year);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/make/{make}")
    public ResponseEntity<List<Car>> getCarsByMake(@PathVariable String make) {
        List<Car> cars = carService.getCarsByMake(make);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<Car>> getCarsByModel(@PathVariable String model) {
        List<Car> cars = carService.getCarsByModel(model);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<Car>> getCarsByYear(@PathVariable Integer year) {
        List<Car> cars = carService.getCarsByYear(year);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Car>> getCarsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Car> cars = carService.getCarsByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/condition/{condition}")
    public ResponseEntity<List<Car>> getCarsByCondition(@PathVariable String condition) {
        List<Car> cars = carService.getCarsByCondition(condition);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/vin/{vin}")
    public ResponseEntity<Car> getCarByVin(@PathVariable String vin) {
        Car car = carService.getCarByVin(vin);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("/low-mileage/{maxMileage}")
    public ResponseEntity<List<Car>> getLowMileageAvailableCars(@PathVariable Integer maxMileage) {
        List<Car> cars = carService.getLowMileageAvailableCars(maxMileage);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/recently-added")
    public ResponseEntity<List<Car>> getRecentlyAddedCars() {
        List<Car> cars = carService.getRecentlyAddedCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
}