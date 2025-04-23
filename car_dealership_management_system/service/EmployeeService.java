package com.cardealership.managementsystem.service;

import com.cardealership.managementsystem.exception.EmployeeNotFoundException;
import com.cardealership.managementsystem.model.Employee;
import com.cardealership.managementsystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhone(employeeDetails.getPhone());
        employee.setPosition(employeeDetails.getPosition());
        employee.setSalary(employeeDetails.getSalary());
        employee.setCommissionRate(employeeDetails.getCommissionRate());

        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByName(String firstName, String lastName) {
        if (firstName != null && lastName != null) {
            return employeeRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
        } else if (firstName != null) {
            return employeeRepository.findByFirstNameIgnoreCase(firstName);
        } else if (lastName != null) {
            return employeeRepository.findByLastNameIgnoreCase(lastName);
        } else {
            return employeeRepository.findAll();
        }
    }

    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        Employee employee = employeeRepository.findByEmailIgnoreCase(email);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found with email: " + email);
        }
        return employee;
    }

    @Transactional(readOnly = true)
    public List<Employee> findByPosition(String position) {
        return employeeRepository.findByPositionIgnoreCase(position);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByHireDateRange(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findByHireDateBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Employee> findBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return employeeRepository.findBySalaryBetween(minSalary, maxSalary);
    }

    @Transactional(readOnly = true)
    public Map<Employee, BigDecimal> findTopSalespeopleByTotalSales() {
        List<Object[]> results = employeeRepository.findTopSalespeopleByTotalSales();
        Map<Employee, BigDecimal> topSalespeople = new HashMap<>();

        for (Object[] result : results) {
            Employee employee = (Employee) result[0];
            BigDecimal totalSales = (BigDecimal) result[1];
            topSalespeople.put(employee, totalSales);
        }

        return topSalespeople;
    }

    @Transactional(readOnly = true)
    public List<Employee> findSalespeopleWithNoSales() {
        return employeeRepository.findSalespeopleWithNoSales();
    }

    @Transactional(readOnly = true)
    public List<Employee> findSalespeopleWithSalesInDateRange(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findSalespeopleWithSalesInDateRange(startDate, endDate);
    }
}