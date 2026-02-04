package com.BigBull.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BigBull.entity.Transaction;
import com.BigBull.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAssetId(@PathVariable Long assetId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAssetId(assetId));
    }
    
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<List<Transaction>> getTransactionsBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(transactionService.getTransactionsBySymbol(symbol));
    }
    
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Map<String, Object> request) {
        try {
            Long assetId = Long.valueOf(request.get("assetId").toString());
            String type = request.get("type").toString();
            Double quantity = Double.valueOf(request.get("quantity").toString());
            Double price = Double.valueOf(request.get("price").toString());
            
            Transaction transaction = transactionService.createTransaction(assetId, type, quantity, price);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }
    
    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("\n========== BUY REQUEST RECEIVED ==========");
            System.out.println("Full Request Body: " + request);
            
            // Validate required fields
            if (!request.containsKey("symbol") || request.get("symbol") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required field: symbol"));
            }
            if (!request.containsKey("name") || request.get("name") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required field: name"));
            }
            if (!request.containsKey("quantity") || request.get("quantity") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required field: quantity"));
            }
            if (!request.containsKey("price") || request.get("price") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required field: price"));
            }
            
            String symbol = request.get("symbol").toString();
            String name = request.get("name").toString();
            Double quantity = Double.valueOf(request.get("quantity").toString());
            Double price = Double.valueOf(request.get("price").toString());
            Double totalCost = quantity * price;
            
            System.out.println("Stock Details:");
            System.out.println("  - Symbol: " + symbol);
            System.out.println("  - Name: " + name);
            System.out.println("  - Quantity: " + quantity);
            System.out.println("  - Price Per Share: $" + price);
            System.out.println("  - Total Cost: $" + totalCost);
            System.out.println("=========================================\n");
            
            Transaction transaction = transactionService.buyStock(symbol, name, quantity, price);
            System.out.println("✓ Buy Transaction Completed Successfully");
            System.out.println("  - Transaction ID: " + transaction.getId());
            System.out.println("=========================================\n");
            return ResponseEntity.ok(transaction);
        } catch (NumberFormatException e) {
            System.err.println("Buy failed - Invalid number format: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid number format for quantity or price"));
        } catch (RuntimeException e) {
            System.err.println("Buy failed - RuntimeException: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Buy failed - Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }
    
    @PostMapping("/sell")
    public ResponseEntity<?> sellStock(@RequestBody Map<String, Object> request) {
        try {
            String symbol = request.get("symbol").toString();
            Double quantity = Double.valueOf(request.get("quantity").toString());
            Double price = Double.valueOf(request.get("price").toString());
            
            Transaction transaction = transactionService.sellStock(symbol, quantity, price);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
