package com.BigBull.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallet")
public class Wallet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Double balance;
    
    // Constructors
    public Wallet() {
        this.balance = 0.0;
    }
    
    public Wallet(Double balance) {
        this.balance = balance;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Double getBalance() {
        return balance;
    }
    
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    
    // Business methods
    public void deposit(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
    }
    
    public void withdraw(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance -= amount;
    }
    
    public boolean hasBalance(Double amount) {
        return this.balance >= amount;
    }
}
