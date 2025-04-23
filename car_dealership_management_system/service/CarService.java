package com.cardealership.managementsystem.service;

import com.cardealership.managementsystem.exception.CarNotFoundException;
import com.cardealership.managementsystem.model.Car;
import com.cardealership.managementsystem.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional(readOnly = true)
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + id));
    }

    @Transactional
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Transactional
    public Car updateCar(Long id, Car carDetails) {
        Car car = getCarById(id);

        car.setMake(carDetails.getMake());
        car.setModel(carDetails.getModel());
        car.setYear(carDetails.getYear());
        car.setVin(carDetails.getVin());
        car.setColor(carDetails.getColor());
        car.setCondition(carDetails.getCondition());
        car.setPrice(carDetails.getPrice());
        car.setMileage(carDetails.getMileage());
        car.setSold(carDetails.getSold());

        return carRepository.save(car);
    }

    @Transactional
    public void deleteCar(Long id) {
        Car car = getCarById(id);
        carRepository.delete(car);
    }

    @Transactional
    public Car markCarAsSold(Long id) {
        Car car = getCarById(id);
        car.setSold(true);
        return carRepository.save(car);
    }

    @Transactional(readOnly = true)
    public List<Car> getAvailableCars() {
        return carRepository.findBySold(false);
    }

    @Transactional(readOnly = true)
    public List<Car> getCarsByMake(String make) {
        return carRepository.findByMakeIgnoreCase(make);
    }

    @Transactional(readOnly = true)
    public List<Car> getCarsByModel(String model) {
        return carRepository.findByModelIgnoreCase(model);
    }

    @Transactional(readOnly = true)
    public List<Car> getCarsByYear(Integer year) {
        return carRepository.findByYear(year);
    }

    @Transactional(readOnly = true)
    public List<Car> getCarsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return carRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Transactional(readOnly = true)
    public List<Car> getCarsByCondition(String condition) {
        return carRepository.findByConditionIgnoreCase(condition);
    }

    @Transactional(readOnly = true)
    public Car getCarByVin(String vin) {
        Car car = carRepository.findByVinIgnoreCase(vin);
        if (car == null) {
            throw new CarNotFoundException("Car not found with VIN: " + vin);
        }
        return car;
    }

    @Transactional(readOnly = true)
    public List<Car> searchCars(String make, String model, Integer year) {
        return carRepository.findByMakeIgnoreCaseAndModelIgnoreCaseAndYear(make, model, year);
    }

    @Transactional(readOnly = true)
    public List<Car> getLowMileageAvailableCars(Integer maxMileage) {
        return carRepository.findLowMileageAvailableCars(maxMileage);
    }

    @Transactional(readOnly = true)
    public List<Car> getRecentlyAddedCars() {
        return carRepository.findRecentlyAddedCars();
    }
}