# BigBull Server API Documentation

Base URL: `http://localhost:8080`

## Table of Contents
- [Assets API](#assets-api)
- [Transactions API](#transactions-api)
- [Portfolio API](#portfolio-api)
- [Wallet API](#wallet-api)
- [Stocks API](#stocks-api)

---

## Assets API

Base Path: `/api/assets`

### 1. Get All Assets
- **Endpoint:** `GET /api/assets`
- **Description:** Retrieve all assets in the portfolio
- **Response:** Array of Asset objects
- **Example:**
  ```bash
  curl http://localhost:8080/api/assets
  ```
- **Response Body:**
  ```json
  [
    {
      "id": 1,
      "type": "STOCK",
      "name": "Apple Inc.",
      "symbol": "AAPL"
    }
  ]
  ```

### 2. Get Asset by ID
- **Endpoint:** `GET /api/assets/{id}`
- **Description:** Retrieve a specific asset by its ID
- **Path Parameter:** `id` (Long) - Asset ID
- **Response:** Asset object or 404 if not found
- **Example:**
  ```bash
  curl http://localhost:8080/api/assets/1
  ```

### 3. Get Asset by Symbol
- **Endpoint:** `GET /api/assets/symbol/{symbol}`
- **Description:** Retrieve a specific asset by its stock symbol
- **Path Parameter:** `symbol` (String) - Asset symbol (e.g., AAPL, TSLA)
- **Response:** Asset object or 404 if not found
- **Example:**
  ```bash
  curl http://localhost:8080/api/assets/symbol/AAPL
  ```

### 4. Create Asset
- **Endpoint:** `POST /api/assets`
- **Description:** Add a new asset to the portfolio
- **Request Body:**
  ```json
  {
    "type": "STOCK",
    "name": "Apple Inc.",
    "symbol": "AAPL"
  }
  ```
- **Response:** Created Asset object
- **Example:**
  ```bash
  curl -X POST http://localhost:8080/api/assets \
    -H "Content-Type: application/json" \
    -d '{"type":"STOCK","name":"Apple Inc.","symbol":"AAPL"}'
  ```

### 5. Delete Asset
- **Endpoint:** `DELETE /api/assets/{id}`
- **Description:** Remove an asset from the portfolio
- **Path Parameter:** `id` (Long) - Asset ID
- **Response:** 204 No Content
- **Example:**
  ```bash
  curl -X DELETE http://localhost:8080/api/assets/1
  ```

---

## Transactions API

Base Path: `/api/transactions`

### 1. Get All Transactions
- **Endpoint:** `GET /api/transactions`
- **Description:** Retrieve all transactions
- **Response:** Array of Transaction objects
- **Example:**
  ```bash
  curl http://localhost:8080/api/transactions
  ```
- **Response Body:**
  ```json
  [
    {
      "id": 1,
      "asset": {
        "id": 1,
        "type": "STOCK",
        "name": "Apple Inc.",
        "symbol": "AAPL"
      },
      "type": "BUY",
      "quantity": 10.0,
      "price": 150.00,
      "transactionDate": "2025-01-15T10:30:00"
    }
  ]
  ```

### 2. Get Transaction by ID
- **Endpoint:** `GET /api/transactions/{id}`
- **Description:** Retrieve a specific transaction by its ID
- **Path Parameter:** `id` (Long) - Transaction ID
- **Response:** Transaction object or 404 if not found
- **Example:**
  ```bash
  curl http://localhost:8080/api/transactions/1
  ```

### 3. Get Transactions by Asset ID
- **Endpoint:** `GET /api/transactions/asset/{assetId}`
- **Description:** Retrieve all transactions for a specific asset
- **Path Parameter:** `assetId` (Long) - Asset ID
- **Response:** Array of Transaction objects
- **Example:**
  ```bash
  curl http://localhost:8080/api/transactions/asset/1
  ```

### 4. Get Transactions by Symbol
- **Endpoint:** `GET /api/transactions/symbol/{symbol}`
- **Description:** Retrieve all transactions for a specific stock symbol
- **Path Parameter:** `symbol` (String) - Asset symbol (e.g., AAPL, TSLA)
- **Response:** Array of Transaction objects
- **Example:**
  ```bash
  curl http://localhost:8080/api/transactions/symbol/AAPL
  ```

### 5. Create Transaction
- **Endpoint:** `POST /api/transactions`
- **Description:** Record a new buy/sell transaction (requires asset to exist first)
- **Request Body:**
  ```json
  {
    "assetId": 1,
    "type": "BUY",
    "quantity": 10.0,
    "price": 150.00
  }
  ```
- **Response:** Created Transaction object (with auto-generated transactionDate)
- **Note:** Transaction type should be either "BUY" or "SELL". Asset must exist in database.
- **Example:**
  ```bash
  curl -X POST http://localhost:8080/api/transactions \
    -H "Content-Type: application/json" \
    -d '{"assetId":1,"type":"BUY","quantity":10.0,"price":150.00}'
  ```

### 6. Buy Stock (Simplified)
- **Endpoint:** `POST /api/transactions/buy`
- **Description:** Buy a stock (automatically creates asset if it doesn't exist)
- **Request Body:**
  ```json
  {
    "symbol": "AAPL",
    "name": "Apple Inc.",
    "quantity": 10.0,
    "price": 150.00
  }
  ```
- **Parameters:**
  - `symbol` (String) - Stock ticker symbol
  - `name` (String) - Company name
  - `quantity` (Double) - Number of shares to buy
  - `price` (Double) - **Price per share** (current market price)
  - **Total Cost** = quantity × price (automatically deducted from wallet)
- **Response:** Created Transaction object
- **Note:** This is the recommended endpoint for buying stocks from the frontend. Get current price from `/api/stocks/quote/{symbol}` before buying.
- **Example:**
  ```bash
  curl -X POST http://localhost:8080/api/transactions/buy \
    -H "Content-Type: application/json" \
    -d '{"symbol":"AAPL","name":"Apple Inc.","quantity":10.0,"price":150.00}'
  ```
  This will deduct $1,500 (10 × $150) from your wallet.

### 7. Sell Stock (Simplified)
- **Endpoint:** `POST /api/transactions/sell`
- **Description:** Sell a stock by symbol
- **Request Body:**
  ```json
  {
    "symbol": "AAPL",
    "quantity": 5.0,
    "price": 170.00
  }
  ```
- **Parameters:**
  - `symbol` (String) - Stock ticker symbol to sell
  - `quantity` (Double) - Number of shares to sell
  - `price` (Double) - **Price per share** (current market price)
  - **Total Credit** = quantity × price (automatically added to wallet)
- **Response:** Created Transaction object
- **Note:** Asset must exist and have sufficient quantity. Get current price from `/api/stocks/quote/{symbol}` before selling.
- **Example:**
  ```bash
  curl -X POST http://localhost:8080/api/transactions/sell \
    -H "Content-Type: application/json" \
    -d '{"symbol":"AAPL","quantity":5.0,"price":170.00}'
  ```
  This will credit $850 (5 × $170) to your wallet.

### 8. Delete Transaction
- **Endpoint:** `DELETE /api/transactions/{id}`
- **Description:** Remove a transaction record
- **Path Parameter:** `id` (Long) - Transaction ID
- **Response:** 204 No Content
- **Example:**
  ```bash
  curl -X DELETE http://localhost:8080/api/transactions/1
  ```

---

## Portfolio API

Base Path: `/api/portfolio`

### 1. Get Portfolio Summary
- **Endpoint:** `GET /api/portfolio/summary`
- **Description:** Get comprehensive portfolio statistics and summary
- **Response:** Portfolio summary object with various metrics
- **Example:**
  ```bash
  curl http://localhost:8080/api/portfolio/summary
  ```
- **Response Body:**
  ```json
  {
    "totalValue": 25000.00,
    "totalInvestment": 20000.00,
    "totalProfit": 5000.00,
    "profitPercentage": 25.0,
    "assetCount": 5,
    "positions": [...]
  }
  ```

---

## Wallet API

Base Path: `/api/wallet`

### 1. Get Wallet Balance
- **Endpoint:** `GET /api/wallet`
- **Description:** Get current wallet balance
- **Response:** Wallet object with balance
- **Example:**
  ```bash
  curl http://localhost:8080/api/wallet
  ```
- **Response Body:**
  ```json
  {
    "id": 1,
    "balance": 10000.00
  }
  ```

### 2. Deposit to Wallet
- **Endpoint:** `POST /api/wallet/deposit`
- **Description:** Add money to wallet
- **Request Body:**
  ```json
  {
    "amount": 5000.00
  }
  ```
- **Response:** Updated Wallet object
- **Example:**
  ```bash
  curl -X POST http://localhost:8080/api/wallet/deposit \
    -H "Content-Type: application/json" \
    -d '{"amount":5000.00}'
  ```

### 3. Withdraw from Wallet
- **Endpoint:** `POST /api/wallet/withdraw`
- **Description:** Withdraw money from wallet
- **Request Body:**
  ```json
  {
    "amount": 1000.00
  }
  ```
- **Response:** Updated Wallet object or 400 if insufficient balance
- **Example:**
  ```bash
  curl -X POST http://localhost:8080/api/wallet/withdraw \
    -H "Content-Type: application/json" \
    -d '{"amount":1000.00}'
  ```

---

## Stocks API

Base Path: `/api/stocks`

**Note:** These en,
  "quantity": 10.0
}
```

### Wallet
```json
{
  "id": 1,
  "balance": 10000.00dpoints proxy requests to a Flask backend service running on `http://localhost:5000`

### 1. Search Stocks
- **Endpoint:** `GET /api/stocks/search`
- **Description:** Search for stocks by query string
- **Query Parameters:**
  - `query` (String, required) - Search term
  - `maxResults` (Integer, optional, default: 5) - Maximum number of results
- **Response:** Search results from market data provider
- **Example:**
  ```bash
  curl "http://localhost:8080/api/stocks/search?query=apple&maxResults=10"
  ```

### 2. Get Stock History
- **Endpoint:** `GET /api/stocks/history/{symbol}`
- **Description:** Get historical price data for a stock
- **Path Parameter:** `symbol` (String) - Stock symbol (e.g., AAPL)
- **Query Parameters:**
  - `timeframe` (String, optional, default: "1M") - Time period (e.g., 1D, 1W, 1M, 3M, 1Y)
- **Response:** Historical price data
- **Example:**
  ```bash
  curl "http://localhost:8080/api/stocks/history/AAPL?timeframe=3M"
  ```

### 3. Get Stock Info
- **Endpoint:** `GET /api/stocks/info/{symbol}`
- **Description:** Get detailed company information and fundamentals
- **Path Parameter:** `symbol` (String) - Stock symbol (e.g., AAPL)
- **Response:** Company information (sector, market cap, description, etc.)
- **Example:**
  ```bash
  curl http://localhost:8080/api/stocks/info/AAPL
  ```

### 4. Get Stock Quote
- **Endpoint:** `GET /api/stocks/quote/{symbol}`
- **Description:** Get current/real-time stock price and quote data
- **Path Parameter:** `symbol` (String) - Stock symbol (e.g., AAPL)
- **Response:** Current stock quote with price, volume, etc.
- **Example:**
  ```bash
  curl http://localhost:8080/api/stocks/quote/AAPL
  ```

---

## Data Models

### Asset
```json
{
  "id": 1,
  "type": "STOCK",
  "name": "Apple Inc.",
  "symbol": "AAPL"
}
```

### Transaction
```json
{
  "id": 1,
  "asset": {
    "id": 1,
    "type": "STOCK",
    "name": "Apple Inc.",
    "symbol": "AAPL"
  },
  "type": "BUY",
  "quantity": 10.0,
  "price": 150.00,
  "transactionDate": "2025-01-15T10:30:00"
}
```

---

## Error Responses

All endpoints may return the following error responses:

- **400 Bad Request** - Invalid request parameters or body
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

---

## CORS Configuration

All endpoints are configured with `@CrossOrigin(origins = "*")` allowing requests from any origin.

---

## Prerequisites

1. **Java Spring Boot Server** - Running on port 8080
2. **Flask Backend Service** (for Stock API) - Running on port 5000
3. **MySQL Database** - Connection configured in `application.properties`

---

## Quick Start Frontend Integration

### JavaScript/Fetch Example
```javascript
// Get all assets
fetch('http://localhost:8080/api/assets')
  .then(response => response.json())
  .then(data => console.log(data));

// Buy stock (simplified - recommended)
fetch('http://localhost:8080/api/transactions/buy', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    symbol: 'AAPL',
    name: 'Apple Inc.',
    quantity: 10.0,
    price: 150.00
  })
})
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(err => console.error('Error:', err));

// Sell stock
fetch('http://localhost:8080/api/transactions/sell', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    symbol: 'AAPL',
    quantity: 5.0,
    price: 170.00
  })
})
  .then(response => response.json())
  .then(data => console.log(data));

// Search stocks
fetch('http://localhost:8080/api/stocks/search?query=apple&maxResults=5')
  .then(response => response.json())
  .then(data => console.log(data));
```

###Get current stock price
const getStockPrice = async (symbol) => {
  const response = await axios.get(`${API_BASE}/stocks/quote/${symbol}`);
  return response.data.price; // Returns current market price
};

// Buy stock (use current market price)
const buyStock = async (symbol, name, quantity) => {
  try {
    // Get current price first
    const currentPrice = await getStockPrice(symbol);
    
    const response = await axios.post(`${API_BASE}/transactions/buy`, {
      symbol,
      name,
      quantity,
      price: currentPrice  // Price per share
    });
    
    console.log(`Bought ${quantity} shares at $${currentPrice} each`);
    console.log(`Total cost: $${quantity * currentPrice}`);
    return response.data;
  } catch (error) {
    console.error('Buy error:', error.response?.data?.error);
    throw error;
  }
};

// Sell stock (use current market price)
const sellStock = async (symbol, quantity) => {
  try {
    // Get current price first
    const currentPrice = await getStockPrice(symbol);
    
    const response = await axios.post(`${API_BASE}/transactions/sell`, {
      symbol,
      quantity,
      price: currentPrice  // Price per share
    });
    
    console.log(`Sold ${quantity} shares at $${currentPrice} each`);
    console.log(`Total credit: $${quantity * currentPrice}`);
    return response.data;
  } catch (error) {
    console.error('Sell error:', error.response?.data?.error);
    throw error;
  }
};

// Get portfolio summary
const getPortfolioSummary = async () => {
  const response = await axios.get(`${API_BASE}/portfolio/summary`);
  return response.data;
};

// Deposit to wallet
const depositToWallet = async (amount) => {
  const response = await axios.post(`${API_BASE}/wallet/deposit`, { amount });
  return response.data;
};

// Example usage:
// await buyStock('AAPL', 'Apple Inc.', 10);  // Buys 10 shares at current price
// await sellStock('AAPL', 5);  // Sells 5 shares at current price

// Deposit to wallet
const depositToWallet = async (amount) => {
  const response = await axios.post(`${API_BASE}/wallet/deposit`, { amount });
  return response.data;
};
```

---

## Complete Workflow Example

### Method 1: Simplified (Recommended for Frontend)
```bash
# 1. Deposit money to wallet
curl -X POST http://localhost:8080/api/wallet/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount":10000.00}'

# 2. Buy stock directly (no need to create asset first)
curl -X POST http://localhost:8080/api/transactions/buy \
  -H "Content-Type: application/json" \
  -d '{"symbol":"AAPL","name":"Apple Inc.","quantity":10.0,"price":150.00}'

# 3. Check portfolio summary
curl http://localhost:8080/api/portfolio/summary

# 4. Sell stock
curl -X POST http://localhost:8080/api/transactions/sell \
  -H "Content-Type: application/json" \
  -d '{"symbol":"AAPL","quantity":5.0,"price":170.00}'
```

### Method 2: Traditional (Create Asset First)
```bash
# 1. Deposit money to wallet
curl -X POST http://localhost:8080/api/wallet/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount":10000.00}'

# 2. Create an asset
curl -X POST http://localhost:8080/api/assets \
  -H "Content-Type: application/json" \
  -d '{"type":"STOCK","name":"Apple Inc.","symbol":"AAPL"}'

# 3. Buy using asset ID
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{"assetId":1,"type":"BUY","quantity":10.0,"price":150.00}'
```

---

## Troubleshooting Transaction Errors

### Common Issues and Solutions

**Problem: 400 Bad Request - "Insufficient wallet balance"**
- **Solution:** Deposit money first using `POST /api/wallet/deposit`
- **Check balance:** `GET /api/wallet`

**Problem: 400 Bad Request - "Insufficient asset quantity"**
- **Solution:** You're trying to sell more than you own
- **Check holdings:** `GET /api/assets` or `GET /api/portfolio/summary`

**Problem: 400 Bad Request - "Asset not found"** (when using `/api/transactions` endpoint)
- **Solution:** Use `/api/transactions/buy` instead (auto-creates asset)
- **Or:** Create asset first using `POST /api/assets`

**Problem: 500 Internal Server Error**
- **Causes:**
  1. Database not connected - Check MySQL is running
  2. Invalid data types - Ensure quantity and price are numbers
  3. Missing required fields - Check all fields are present

### Using the Right Endpoint

**For Buying (Frontend):**
✅ **Use:** `POST /api/transactions/buy` with `{symbol, name, quantity, price}`
- Auto-creates asset if doesn't exist
- Simpler and more frontend-friendly

❌ **Avoid:** `POST /api/transactions` with `{assetId, type, quantity, price}`
- Requires creating asset first
- More complex workflow

**For Selling:**
✅ **Use:** `POST /api/transactions/sell` with `{symbol, quantity, price}`
- Works with stock symbol
- Simpler

### Frontend Checklist
1. ✅ Wallet has sufficient balance (deposit first if needed)
2. ✅ Using `/api/transactions/buy` endpoint (not `/api/transactions`)
7. **Price Parameter**: 
   - `price` = **price per share/unit** (NOT total cost)
   - Total cost/credit = `quantity × price`
   - Always get current price from `/api/stocks/quote/{symbol}` before transactions
   - Example: Buying 10 shares at $150 each = $1,500 total deducted from wallet
8. **Transaction Flow**:
   - **Buy**: Get current price → Call `/api/transactions/buy` → Wallet deducted
   - **Sell**: Get current price → Call `/api/transactions/sell` → Wallet credited
3. ✅ Sending symbol, name, quantity, and price
4. ✅ Handling error responses properly

---
7. **Wallet Integration**: 
   - BUY transactions automatically deduct from wallet (quantity × price)
   - SELL transactions automatically credit to wallet (quantity × price)
   - Transactions will fail if insufficient wallet balance or asset quantity
8. **Asset Quantity Management**:
   - Asset quantity automatically increases on BUY
   - Asset quantity automatically decreases on SELL
   - Assets are automatically deleted when quantity reaches 0
9. **Initial Setup**: Deposit money to wallet before making purchase transactions

## Notes for Frontend Development

1. **Asset Types**: Currently supports "STOCK" and "CRYPTO" types
2. **Transaction Types**: Only "BUY" and "SELL" are supported
3. **Date Format**: All dates are in ISO 8601 format (e.g., "2025-01-15T10:30:00")
4. **Timeframe Options**: Common values are "1D", "1W", "1M", "3M", "6M", "1Y"
5. **Stock Symbols**: Use standard ticker symbols (e.g., AAPL, TSLA, MSFT)
6. **Dependencies**: Stock API endpoints require Flask backend to be running
