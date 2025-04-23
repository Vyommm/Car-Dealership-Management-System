package com.cardealership.managementsystem.repository;

import com.cardealership.managementsystem.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    // Find cars by make
    List<Car> findByMakeIgnoreCase(String make);

    // Find cars by model
    List<Car> findByModelIgnoreCase(String model);

    // Find cars by year
    List<Car> findByYear(Integer year);

    // Find cars by price range
    List<Car> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find cars by condition
    List<Car> findByConditionIgnoreCase(String condition);

    // Find cars by availability (sold or not)
    List<Car> findBySold(Boolean sold);

    // Search cars by make, model, and year
    List<Car> findByMakeIgnoreCaseAndModelIgnoreCaseAndYear(String make, String model, Integer year);

    // Find cars by VIN
    Car findByVinIgnoreCase(String vin);

    // Custom query to find cars with mileage less than specified value and not sold
    @Query("SELECT c FROM Car c WHERE c.mileage < ?1 AND c.sold = false")
    List<Car> findLowMileageAvailableCars(Integer maxMileage);

    // Custom query to find recently added cars
    @Query("SELECT c FROM Car c WHERE c.sold = false ORDER BY c.dateAdded DESC")
    List<Car> findRecentlyAddedCars();
}