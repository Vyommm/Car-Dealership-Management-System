package com.cardealership.managementsystem.repository;

import com.cardealership.managementsystem.model.Car;
import com.cardealership.managementsystem.model.Customer;
import com.cardealership.managementsystem.model.Employee;
import com.cardealership.managementsystem.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Find sales by customer
    List<Sale> findByCustomer(Customer customer);

    // Find sales by salesperson
    List<Sale> findBySalesperson(Employee salesperson);

    // Find sales by car
    List<Sale> findByCar(Car car);

    // Find sales by sale date
    List<Sale> findBySaleDate(LocalDate saleDate);

    // Find sales by sale date range
    List<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    // Find sales by total price range
    List<Sale> findByTotalPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find sales by payment method
    List<Sale> findByPaymentMethodIgnoreCase(String paymentMethod);

    // Find sales by status
    List<Sale> findBySaleStatusIgnoreCase(String saleStatus);

    // Custom query to find sales with total price greater than specified value
    @Query("SELECT s FROM Sale s WHERE s.totalPrice > ?1")
    List<Sale> findSalesWithTotalPriceGreaterThan(BigDecimal price);

    // Custom query to find monthly sales totals
    @Query("SELECT FUNCTION('YEAR', s.saleDate) as year, FUNCTION('MONTH', s.saleDate) as month, " +
            "SUM(s.totalPrice) as total, COUNT(s) as count FROM Sale s " +
            "GROUP BY FUNCTION('YEAR', s.saleDate), FUNCTION('MONTH', s.saleDate) " +
            "ORDER BY FUNCTION('YEAR', s.saleDate) DESC, FUNCTION('MONTH', s.saleDate) DESC")
    List<Object[]> findMonthlySalesTotals();

    // Custom query to find today's sales
    @Query("SELECT s FROM Sale s WHERE s.saleDate = CURRENT_DATE")
    List<Sale> findTodaySales();
}