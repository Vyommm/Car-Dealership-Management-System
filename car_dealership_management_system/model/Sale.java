package com.cardealership.managementsystem.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salesperson_id")
    private Employee salesperson;

    private LocalDate saleDate;
    private BigDecimal salePrice;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private String paymentMethod;
    private String saleStatus; // Pending, Completed, Cancelled

    // Constructors
    public Sale() {
        this.saleDate = LocalDate.now();
    }

    public Sale(Car car, Customer customer, Employee salesperson, BigDecimal salePrice,
                BigDecimal tax, String paymentMethod) {
        this.car = car;
        this.customer = customer;
        this.salesperson = salesperson;
        this.saleDate = LocalDate.now();
        this.salePrice = salePrice;
        this.tax = tax;
        this.totalPrice = salePrice.add(tax);
        this.paymentMethod = paymentMethod;
        this.saleStatus = "Completed";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getSalesperson() {
        return salesperson;
    }

    public void setSalesperson(Employee salesperson) {
        this.salesperson = salesperson;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
        recalculateTotalPrice();
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
        recalculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    private void recalculateTotalPrice() {
        if (salePrice != null && tax != null) {
            this.totalPrice = salePrice.add(tax);
        }
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public BigDecimal calculateCommission() {
        if (salesperson != null && salesperson.getCommissionRate() != null && salePrice != null) {
            return salePrice.multiply(salesperson.getCommissionRate());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "Sale #" + id + ": " + car + " to " + customer + " by " + salesperson;
    }
}