# Wallet API - Frontend Integration Guide

## Overview
The Wallet API manages user funds for stock trading. It provides endpoints to check balance, deposit money, and withdraw money. The wallet is automatically integrated with buy/sell transactions.

---

## Base URL
```
http://localhost:8080/api/wallet
```

---

## Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/wallet` | Get current wallet balance |
| POST | `/api/wallet/deposit` | Add money to wallet |
| POST | `/api/wallet/withdraw` | Remove money from wallet |

---

## 1. Get Wallet Balance

Retrieve the current wallet balance.

**Endpoint:** `GET /api/wallet`

**Request:** No parameters required

**Success Response (200):**
```json
{
  "id": 1,
  "balance": 10000.00
}
```

**Frontend Example:**
```javascript
// Using Fetch API
const getWalletBalance = async () => {
  const response = await fetch('http://localhost:8080/api/wallet');
  const data = await response.json();
  return data.balance;
};

// Using Axios
import axios from 'axios';

const getWalletBalance = async () => {
  const response = await axios.get('http://localhost:8080/api/wallet');
  return response.data.balance;
};
```

---

## 2. Deposit Money

Add funds to the wallet.

**Endpoint:** `POST /api/wallet/deposit`

**Request Body:**
```json
{
  "amount": 5000.00
}
```

**Field Requirements:**
- `amount` (Number, required) - Must be positive
- Supports decimal values (e.g., 1000.50)

**Success Response (200):**
```json
{
  "id": 1,
  "balance": 15000.00
}
```

**Error Response (400):**
```json
{
  "error": "Deposit amount must be positive"
}
```

**Frontend Example:**
```javascript
// Using Fetch API
const depositToWallet = async (amount) => {
  const response = await fetch('http://localhost:8080/api/wallet/deposit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ amount })
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.error);
  }
  
  return await response.json();
};

// Using Axios
const depositToWallet = async (amount) => {
  try {
    const response = await axios.post('http://localhost:8080/api/wallet/deposit', {
      amount: parseFloat(amount)
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.error || 'Deposit failed');
  }
};

// Usage
await depositToWallet(5000);
```

---

## 3. Withdraw Money

Remove funds from the wallet.

**Endpoint:** `POST /api/wallet/withdraw`

**Request Body:**
```json
{
  "amount": 1000.00
}
```

**Field Requirements:**
- `amount` (Number, required) - Must be positive
- Must not exceed current balance
- Supports decimal values

**Success Response (200):**
```json
{
  "id": 1,
  "balance": 14000.00
}
```

**Error Responses (400):**

Insufficient balance:
```json
{
  "error": "Insufficient balance"
}
```

Invalid amount:
```json
{
  "error": "Withdrawal amount must be positive"
}
```

**Frontend Example:**
```javascript
// Using Fetch API
const withdrawFromWallet = async (amount) => {
  const response = await fetch('http://localhost:8080/api/wallet/withdraw', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ amount })
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.error);
  }
  
  return await response.json();
};

// Using Axios
const withdrawFromWallet = async (amount) => {
  try {
    const response = await axios.post('http://localhost:8080/api/wallet/withdraw', {
      amount: parseFloat(amount)
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.error || 'Withdrawal failed');
  }
};

// Usage
await withdrawFromWallet(1000);
```

---

## Complete React Component Example

```javascript
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const WalletManager = () => {
  const [balance, setBalance] = useState(0);
  const [amount, setAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const API_URL = 'http://localhost:8080/api/wallet';

  // Fetch wallet balance on component mount
  useEffect(() => {
    fetchBalance();
  }, []);

  const fetchBalance = async () => {
    try {
      const response = await axios.get(API_URL);
      setBalance(response.data.balance);
    } catch (err) {
      setError('Failed to fetch wallet balance');
    }
  };

  const handleDeposit = async () => {
    if (!amount || parseFloat(amount) <= 0) {
      setError('Please enter a valid amount');
      return;
    }

    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const response = await axios.post(`${API_URL}/deposit`, {
        amount: parseFloat(amount)
      });
      setBalance(response.data.balance);
      setSuccess(`Successfully deposited $${amount}`);
      setAmount('');
    } catch (err) {
      setError(err.response?.data?.error || 'Deposit failed');
    } finally {
      setLoading(false);
    }
  };

  const handleWithdraw = async () => {
    if (!amount || parseFloat(amount) <= 0) {
      setError('Please enter a valid amount');
      return;
    }

    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const response = await axios.post(`${API_URL}/withdraw`, {
        amount: parseFloat(amount)
      });
      setBalance(response.data.balance);
      setSuccess(`Successfully withdrew $${amount}`);
      setAmount('');
    } catch (err) {
      setError(err.response?.data?.error || 'Withdrawal failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="wallet-manager">
      <h2>Wallet Balance</h2>
      <div className="balance">
        ${balance.toFixed(2)}
      </div>

      <div className="input-group">
        <input
          type="number"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          placeholder="Enter amount"
          min="0"
          step="0.01"
          disabled={loading}
        />
      </div>

      <div className="button-group">
        <button 
          onClick={handleDeposit} 
          disabled={loading || !amount}
        >
          {loading ? 'Processing...' : 'Deposit'}
        </button>

        <button 
          onClick={handleWithdraw} 
          disabled={loading || !amount}
        >
          {loading ? 'Processing...' : 'Withdraw'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}
    </div>
  );
};

export default WalletManager;
```

---

## Integration with Trading

The wallet is **automatically integrated** with buy/sell transactions:

### Buying Stocks
When you buy a stock, the wallet is automatically debited.

```javascript
// Buy stock - wallet automatically deducted
const buyStock = async (symbol, name, quantity, pricePerShare) => {
  // Total cost = quantity × pricePerShare
  // This amount will be deducted from wallet
  
  const response = await axios.post('http://localhost:8080/api/transactions/buy', {
    symbol: symbol,
    name: name,
    quantity: quantity,
    price: pricePerShare  // Price per share, NOT total
  });
  
  // Refresh wallet balance after purchase
  await fetchBalance();
  
  return response.data;
};

// Example: Buy 10 shares of AAPL at $150 each
// Total deducted: 10 × $150 = $1,500
await buyStock('AAPL', 'Apple Inc.', 10, 150);
```

### Selling Stocks
When you sell a stock, the wallet is automatically credited.

```javascript
// Sell stock - wallet automatically credited
const sellStock = async (symbol, quantity, pricePerShare) => {
  // Total credit = quantity × pricePerShare
  // This amount will be added to wallet
  
  const response = await axios.post('http://localhost:8080/api/transactions/sell', {
    symbol: symbol,
    quantity: quantity,
    price: pricePerShare  // Price per share, NOT total
  });
  
  // Refresh wallet balance after sale
  await fetchBalance();
  
  return response.data;
};

// Example: Sell 5 shares of AAPL at $170 each
// Total credited: 5 × $170 = $850
await sellStock('AAPL', 5, 170);
```

---

## Validation Rules

1. **Positive Amounts Only**
   - Both deposit and withdrawal amounts must be > 0
   - Decimal values are allowed (e.g., 1000.50)

2. **Sufficient Balance**
   - Cannot withdraw more than current balance
   - Buy transactions will fail if balance < (quantity × price)

3. **Number Format**
   - Always use `parseFloat()` or `Number()` when sending amounts
   - Do not send strings like `"1000"` - send numbers like `1000`

4. **Initial State**
   - Wallet is created automatically with balance = 0
   - Must deposit money before making any purchases

---

## Error Handling Best Practices

```javascript
const handleWalletOperation = async (operation, amount) => {
  try {
    setLoading(true);
    setError('');
    
    const response = await axios.post(
      `http://localhost:8080/api/wallet/${operation}`,
      { amount: parseFloat(amount) }
    );
    
    // Success
    setBalance(response.data.balance);
    showSuccessMessage(`${operation} successful`);
    
  } catch (error) {
    // Handle different error types
    if (error.response) {
      // Server responded with error
      const errorMessage = error.response.data.error;
      
      if (errorMessage.includes('Insufficient balance')) {
        showError('You do not have enough balance for this withdrawal');
      } else if (errorMessage.includes('must be positive')) {
        showError('Amount must be greater than zero');
      } else {
        showError(errorMessage);
      }
    } else if (error.request) {
      // Request made but no response
      showError('Server is not responding. Please try again later.');
    } else {
      // Other errors
      showError('An unexpected error occurred');
    }
  } finally {
    setLoading(false);
  }
};
```

---

## Testing with Postman

### 1. Get Balance
```
GET http://localhost:8080/api/wallet
```

### 2. Deposit Money
```
POST http://localhost:8080/api/wallet/deposit
Content-Type: application/json

{
  "amount": 10000
}
```

### 3. Withdraw Money
```
POST http://localhost:8080/api/wallet/withdraw
Content-Type: application/json

{
  "amount": 1000
}
```

### 4. Test Insufficient Balance
```
POST http://localhost:8080/api/wallet/withdraw
Content-Type: application/json

{
  "amount": 999999
}
```
Expected: `400 Bad Request` with error message

---

## Common Issues & Solutions

### Issue: "Insufficient wallet balance" during stock purchase
**Solution:** Deposit money first using `/api/wallet/deposit`

### Issue: Amount shows as null in request
**Solution:** Use `parseFloat(amount)` to convert string to number

### Issue: Balance not updating in UI
**Solution:** Call `fetchBalance()` after deposit/withdraw/buy/sell operations

### Issue: "Deposit amount must be positive"
**Solution:** Ensure amount > 0 before sending request

---

## Response Format

All wallet endpoints return the same response structure:

```typescript
interface WalletResponse {
  id: number;        // Wallet ID
  balance: number;   // Current balance (decimal)
}
```

All errors return:

```typescript
interface ErrorResponse {
  error: string;     // Human-readable error message
}
```

---

## TypeScript Types

```typescript
// Request types
interface DepositRequest {
  amount: number;
}

interface WithdrawRequest {
  amount: number;
}

// Response types
interface Wallet {
  id: number;
  balance: number;
}

interface WalletError {
  error: string;
}

// API functions
const getWalletBalance = async (): Promise<Wallet> => {
  const response = await axios.get<Wallet>('http://localhost:8080/api/wallet');
  return response.data;
};

const depositToWallet = async (amount: number): Promise<Wallet> => {
  const response = await axios.post<Wallet>(
    'http://localhost:8080/api/wallet/deposit',
    { amount }
  );
  return response.data;
};

const withdrawFromWallet = async (amount: number): Promise<Wallet> => {
  const response = await axios.post<Wallet>(
    'http://localhost:8080/api/wallet/withdraw',
    { amount }
  );
  return response.data;
};
```

---

## Prerequisites

1. **Backend Server Running:** Spring Boot server must be running on port 8080
2. **Database Connected:** MySQL database must be connected
3. **CORS Enabled:** API allows requests from any origin (*)

---

## Support

For issues or questions, refer to the main [API_DOCUMENTATION.md](API_DOCUMENTATION.md) file.
