package com.cardealership.managementsystem.controller;

import com.cardealership.managementsystem.model.Customer;
import com.cardealership.managementsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        List<Customer> customers = customerService.findByName(firstName, lastName);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        Customer customer = customerService.findByEmail(email);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<Customer> getCustomerByPhone(@PathVariable String phone) {
        Customer customer = customerService.findByPhone(phone);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/with-purchases")
    public ResponseEntity<List<Customer>> getCustomersWithPurchases() {
        List<Customer> customers = customerService.findCustomersWithPurchases();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/registered-after")
    public ResponseEntity<List<Customer>> getCustomersRegisteredAfter(@RequestParam LocalDate date) {
        List<Customer> customers = customerService.findCustomersRegisteredAfter(date);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/top-by-purchases")
    public ResponseEntity<Map<Customer, Long>> getTopCustomersByPurchaseCount() {
        Map<Customer, Long> topCustomers = customerService.findTopCustomersByPurchaseCount();
        return new ResponseEntity<>(topCustomers, HttpStatus.OK);
    }
}