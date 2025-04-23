package com.cardealership.managementsystem.service;

import com.cardealership.managementsystem.exception.CarNotFoundException;
import com.cardealership.managementsystem.exception.CustomerNotFoundException;
import com.cardealership.managementsystem.exception.EmployeeNotFoundException;
import com.cardealership.managementsystem.exception.SaleNotFoundException;
import com.cardealership.managementsystem.exception.SaleProcessingException;
import com.cardealership.managementsystem.model.Car;
import com.cardealership.managementsystem.model.Customer;
import com.cardealership.managementsystem.model.Employee;
import com.cardealership.managementsystem.model.Sale;
import com.cardealership.managementsystem.repository.CarRepository;
import com.cardealership.managementsystem.repository.CustomerRepository;
import com.cardealership.managementsystem.repository.EmployeeRepository;
import com.cardealership.managementsystem.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository, CarRepository carRepository,
                       CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.saleRepository = saleRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + id));
    }

    @Transactional
    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    @Transactional
    public Sale updateSale(Long id, Sale saleDetails) {
        Sale sale = getSaleById(id);

        sale.setCar(saleDetails.getCar());
        sale.setCustomer(saleDetails.getCustomer());
        sale.setSalesperson(saleDetails.getSalesperson());
        sale.setSaleDate(saleDetails.getSaleDate());
        sale.setSalePrice(saleDetails.getSalePrice());
        sale.setTax(saleDetails.getTax());
        sale.setPaymentMethod(saleDetails.getPaymentMethod());
        sale.setSaleStatus(saleDetails.getSaleStatus());

        return saleRepository.save(sale);
    }

    @Transactional
    public void deleteSale(Long id) {
        Sale sale = getSaleById(id);

        // Update car status if sale is being deleted
        if (sale.getCar() != null && sale.getCar().getSold()) {
            Car car = sale.getCar();
            car.setSold(false);
            carRepository.save(car);
        }

        saleRepository.delete(sale);
    }

    @Transactional
    public Sale processSale(Long carId, Long customerId, Long salespersonId,
                            BigDecimal salePrice, BigDecimal tax, String paymentMethod) {
        // Validate car
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + carId));

        // Check if car is already sold
        if (car.getSold()) {
            throw new SaleProcessingException("Car is already sold");
        }

        // Validate customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

        // Validate salesperson
        Employee salesperson = employeeRepository.findById(salespersonId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + salespersonId));

        // Check if employee is a salesperson
        if (!salesperson.getPosition().equalsIgnoreCase("Salesperson")) {
            throw new SaleProcessingException("Employee is not a salesperson");
        }

        // Create sale
        Sale sale = new Sale(car, customer, salesperson, salePrice, tax, paymentMethod);

        // Update car status
        car.setSold(true);
        carRepository.save(car);

        // Save and return the sale
        return saleRepository.save(sale);
    }

    @Transactional(readOnly = true)
    public List<Sale> getSalesByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        return saleRepository.findByCustomer(customer);
    }

    @Transactional(readOnly = true)
    public List<Sale> getSalesBySalesperson(Long salespersonId) {
        Employee salesperson = employeeRepository.findById(salespersonId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + salespersonId));
        return saleRepository.findBySalesperson(salesperson);
    }

    @Transactional(readOnly = true)
    public Sale getSaleByCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + carId));
        List<Sale> sales = saleRepository.findByCar(car);

        if (sales.isEmpty()) {
            throw new SaleNotFoundException("No sale found for car with id: " + carId);
        }

        return sales.get(0);
    }

    @Transactional(readOnly = true)
    public List<Sale> getSalesByDate(LocalDate date) {
        return saleRepository.findBySaleDate(date);
    }

    @Transactional(readOnly = true)
    public List<Sale> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return saleRepository.findBySaleDateBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Sale> getSalesByPaymentMethod(String paymentMethod) {
        return saleRepository.findByPaymentMethodIgnoreCase(paymentMethod);
    }

    @Transactional(readOnly = true)
    public List<Sale> getSalesByStatus(String status) {
        return saleRepository.findBySaleStatusIgnoreCase(status);
    }

    @Transactional(readOnly = true)
    public List<Sale> getSalesWithTotalPriceGreaterThan(BigDecimal price) {
        return saleRepository.findSalesWithTotalPriceGreaterThan(price);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMonthlySalesTotals() {
        List<Object[]> monthlySalesTotals = saleRepository.findMonthlySalesTotals();
        Map<String, Object> results = new HashMap<>();

        for (Object[] result : monthlySalesTotals) {
            Integer year = (Integer) result[0];
            Integer month = (Integer) result[1];
            BigDecimal total = (BigDecimal) result[2];
            Long count = (Long) result[3];

            String monthKey = year + "-" + (month < 10 ? "0" + month : month);
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("total", total);
            monthData.put("count", count);

            results.put(monthKey, monthData);
        }

        return results;
    }

    @Transactional(readOnly = true)
    public List<Sale> getTodaySales() {
        return saleRepository.findTodaySales();
    }
}