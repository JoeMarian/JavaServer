package com.BigBull.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BigBull.entity.Wallet;
import com.BigBull.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "*")
public class WalletController {
    
    @Autowired
    private WalletService walletService;
    
    @GetMapping
    public ResponseEntity<Wallet> getWallet() {
        return ResponseEntity.ok(walletService.getWallet());
    }
    
    @PostMapping("/deposit")
    public ResponseEntity<Wallet> deposit(@RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Wallet wallet = walletService.deposit(amount);
        return ResponseEntity.ok(wallet);
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<Wallet> withdraw(@RequestBody Map<String, Double> request) {
        try {
            Double amount = request.get("amount");
            if (amount == null || amount <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Wallet wallet = walletService.withdraw(amount);
            return ResponseEntity.ok(wallet);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
