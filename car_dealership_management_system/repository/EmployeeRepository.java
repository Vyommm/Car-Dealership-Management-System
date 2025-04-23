package com.cardealership.managementsystem.repository;

import com.cardealership.managementsystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find employees by first name
    List<Employee> findByFirstNameIgnoreCase(String firstName);

    // Find employees by last name
    List<Employee> findByLastNameIgnoreCase(String lastName);

    // Find employees by email
    Employee findByEmailIgnoreCase(String email);

    // Find employees by position
    List<Employee> findByPositionIgnoreCase(String position);

    // Find employees by hire date range
    List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    // Find employees by salary range
    List<Employee> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);

    // Find employees by first and last name
    List<Employee> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    // Custom query to find top performing salespeople by sales amount
    @Query("SELECT e, SUM(s.totalPrice) FROM Employee e JOIN e.sales s " +
            "GROUP BY e ORDER BY SUM(s.totalPrice) DESC")
    List<Object[]> findTopSalespeopleByTotalSales();

    // Custom query to find employees with no sales
    @Query("SELECT e FROM Employee e LEFT JOIN e.sales s WHERE s.id IS NULL AND e.position = 'Salesperson'")
    List<Employee> findSalespeopleWithNoSales();

    // Custom query to find salespeople with sales in a given date range
    @Query("SELECT DISTINCT e FROM Employee e JOIN e.sales s WHERE s.saleDate BETWEEN ?1 AND ?2")
    List<Employee> findSalespeopleWithSalesInDateRange(LocalDate startDate, LocalDate endDate);
}