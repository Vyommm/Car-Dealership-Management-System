package com.cardealership.managementsystem.service;

import com.cardealership.managementsystem.exception.CustomerNotFoundException;
import com.cardealership.managementsystem.model.Customer;
import com.cardealership.managementsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    @Transactional
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);

        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhone(customerDetails.getPhone());
        customer.setAddress(customerDetails.getAddress());

        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    @Transactional(readOnly = true)
    public List<Customer> findByName(String firstName, String lastName) {
        if (firstName != null && lastName != null) {
            return customerRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
        } else if (firstName != null) {
            return customerRepository.findByFirstNameIgnoreCase(firstName);
        } else if (lastName != null) {
            return customerRepository.findByLastNameIgnoreCase(lastName);
        } else {
            return customerRepository.findAll();
        }
    }

    @Transactional(readOnly = true)
    public Customer findByEmail(String email) {
        Customer customer = customerRepository.findByEmailIgnoreCase(email);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with email: " + email);
        }
        return customer;
    }

    @Transactional(readOnly = true)
    public Customer findByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with phone: " + phone);
        }
        return customer;
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersWithPurchases() {
        return customerRepository.findCustomersWithPurchases();
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersRegisteredAfter(LocalDate date) {
        return customerRepository.findByRegistrationDateAfter(date);
    }

    @Transactional(readOnly = true)
    public Map<Customer, Long> findTopCustomersByPurchaseCount() {
        List<Object[]> results = customerRepository.findTopCustomersByPurchaseCount();
        Map<Customer, Long> topCustomers = new HashMap<>();

        for (Object[] result : results) {
            Customer customer = (Customer) result[0];
            Long purchaseCount = (Long) result[1];
            topCustomers.put(customer, purchaseCount);
        }

        return topCustomers;
    }
}