package com.cardealership.managementsystem.controller;

import com.cardealership.managementsystem.model.Car;
import com.cardealership.managementsystem.model.Customer;
import com.cardealership.managementsystem.model.Employee;
import com.cardealership.managementsystem.model.Sale;
import com.cardealership.managementsystem.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Sale sale = saleService.getSaleById(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        Sale savedSale = saleService.saveSale(sale);
        return new ResponseEntity<>(savedSale, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        Sale updatedSale = saleService.updateSale(id, sale);
        return new ResponseEntity<>(updatedSale, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/process")
    public ResponseEntity<Sale> processSale(
            @RequestParam Long carId,
            @RequestParam Long customerId,
            @RequestParam Long salespersonId,
            @RequestParam BigDecimal salePrice,
            @RequestParam BigDecimal tax,
            @RequestParam String paymentMethod) {
        Sale sale = saleService.processSale(carId, customerId, salespersonId, salePrice, tax, paymentMethod);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Sale>> getSalesByCustomer(@PathVariable Long customerId) {
        List<Sale> sales = saleService.getSalesByCustomer(customerId);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/salesperson/{salespersonId}")
    public ResponseEntity<List<Sale>> getSalesBySalesperson(@PathVariable Long salespersonId) {
        List<Sale> sales = saleService.getSalesBySalesperson(salespersonId);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<Sale> getSaleByCar(@PathVariable Long carId) {
        Sale sale = saleService.getSaleByCar(carId);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Sale>> getSalesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Sale> sales = saleService.getSalesByDate(date);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Sale>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Sale> sales = saleService.getSalesByDateRange(startDate, endDate);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/payment-method/{paymentMethod}")
    public ResponseEntity<List<Sale>> getSalesByPaymentMethod(@PathVariable String paymentMethod) {
        List<Sale> sales = saleService.getSalesByPaymentMethod(paymentMethod);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Sale>> getSalesByStatus(@PathVariable String status) {
        List<Sale> sales = saleService.getSalesByStatus(status);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/total-price-greater-than/{price}")
    public ResponseEntity<List<Sale>> getSalesWithTotalPriceGreaterThan(@PathVariable BigDecimal price) {
        List<Sale> sales = saleService.getSalesWithTotalPriceGreaterThan(price);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/monthly-totals")
    public ResponseEntity<Map<String, Object>> getMonthlySalesTotals() {
        Map<String, Object> monthlySalesTotals = saleService.getMonthlySalesTotals();
        return new ResponseEntity<>(monthlySalesTotals, HttpStatus.OK);
    }

    @GetMapping("/today")
    public ResponseEntity<List<Sale>> getTodaySales() {
        List<Sale> sales = saleService.getTodaySales();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }
}