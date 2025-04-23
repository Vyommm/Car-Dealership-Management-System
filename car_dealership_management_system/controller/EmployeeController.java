package com.cardealership.managementsystem.controller;

import com.cardealership.managementsystem.model.Employee;
import com.cardealership.managementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        List<Employee> employees = employeeService.findByName(firstName, lastName);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        Employee employee = employeeService.findByEmail(email);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<Employee>> getEmployeesByPosition(@PathVariable String position) {
        List<Employee> employees = employeeService.findByPosition(position);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/hire-date-range")
    public ResponseEntity<List<Employee>> getEmployeesByHireDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<Employee> employees = employeeService.findByHireDateRange(startDate, endDate);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/salary-range")
    public ResponseEntity<List<Employee>> getEmployeesBySalaryRange(
            @RequestParam BigDecimal minSalary,
            @RequestParam BigDecimal maxSalary) {
        List<Employee> employees = employeeService.findBySalaryRange(minSalary, maxSalary);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/top-salespeople")
    public ResponseEntity<Map<Employee, BigDecimal>> getTopSalespeopleByTotalSales() {
        Map<Employee, BigDecimal> topSalespeople = employeeService.findTopSalespeopleByTotalSales();
        return new ResponseEntity<>(topSalespeople, HttpStatus.OK);
    }

    @GetMapping("/salespeople-no-sales")
    public ResponseEntity<List<Employee>> getSalespeopleWithNoSales() {
        List<Employee> employees = employeeService.findSalespeopleWithNoSales();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/salespeople-with-sales-in-date-range")
    public ResponseEntity<List<Employee>> getSalespeopleWithSalesInDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<Employee> employees = employeeService.findSalespeopleWithSalesInDateRange(startDate, endDate);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}