package com.BigBull.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BigBull.entity.Wallet;
import com.BigBull.repository.WalletRepository;

@Service
public class WalletService {
    
    @Autowired
    private WalletRepository walletRepository;
    
    public Wallet getOrCreateWallet() {
        List<Wallet> wallets = walletRepository.findAll();
        if (wallets.isEmpty()) {
            Wallet wallet = new Wallet(0.0);
            return walletRepository.save(wallet);
        }
        return wallets.get(0);
    }
    
    public Wallet getWallet() {
        return getOrCreateWallet();
    }
    
    public Wallet deposit(Double amount) {
        Wallet wallet = getOrCreateWallet();
        wallet.deposit(amount);
        return walletRepository.save(wallet);
    }
    
    public Wallet withdraw(Double amount) {
        Wallet wallet = getOrCreateWallet();
        wallet.withdraw(amount);
        return walletRepository.save(wallet);
    }
    
    public boolean hasBalance(Double amount) {
        Wallet wallet = getOrCreateWallet();
        return wallet.hasBalance(amount);
    }
    
    public Wallet deductBalance(Double amount) {
        Wallet wallet = getOrCreateWallet();
        if (!wallet.hasBalance(amount)) {
            throw new IllegalArgumentException("Insufficient wallet balance");
        }
        wallet.withdraw(amount);
        return walletRepository.save(wallet);
    }
    
    public Wallet creditBalance(Double amount) {
        Wallet wallet = getOrCreateWallet();
        wallet.deposit(amount);
        return walletRepository.save(wallet);
    }
}
