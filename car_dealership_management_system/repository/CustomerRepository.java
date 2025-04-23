package com.cardealership.managementsystem.repository;

import com.cardealership.managementsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Find customers by first name
    List<Customer> findByFirstNameIgnoreCase(String firstName);

    // Find customers by last name
    List<Customer> findByLastNameIgnoreCase(String lastName);

    // Find customers by email
    Customer findByEmailIgnoreCase(String email);

    // Find customers by phone
    Customer findByPhone(String phone);

    // Find customers by name (both first and last)
    List<Customer> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    // Custom query to find customers with purchases
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.purchases p")
    List<Customer> findCustomersWithPurchases();

    // Custom query to find customers registered after a certain date
    List<Customer> findByRegistrationDateAfter(LocalDate date);

    // Custom query to find top customers by number of purchases
    @Query("SELECT c, COUNT(p) FROM Customer c JOIN c.purchases p GROUP BY c ORDER BY COUNT(p) DESC")
    List<Object[]> findTopCustomersByPurchaseCount();
}