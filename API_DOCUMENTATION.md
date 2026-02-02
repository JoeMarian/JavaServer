# BigBull Server API Documentation

Base URL: `http://localhost:8080`

## Table of Contents
- [Assets API](#assets-api)
- [Transactions API](#transactions-api)
- [Portfolio API](#portfolio-api)
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
- **Description:** Record a new buy/sell transaction
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
- **Note:** Transaction type should be either "BUY" or "SELL"
- **Example:**
  ```bash
  curl -X POST http://localhost:8080/api/transactions \
    -H "Content-Type: application/json" \
    -d '{"assetId":1,"type":"BUY","quantity":10.0,"price":150.00}'
  ```

### 6. Delete Transaction
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

## Stocks API

Base Path: `/api/stocks`

**Note:** These endpoints proxy requests to a Flask backend service running on `http://localhost:5000`

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

// Create a transaction
fetch('http://localhost:8080/api/transactions', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    assetId: 1,
    type: 'BUY',
    quantity: 10.0,
    price: 150.00
  })
})
  .then(response => response.json())
  .then(data => console.log(data));

// Search stocks
fetch('http://localhost:8080/api/stocks/search?query=apple&maxResults=5')
  .then(response => response.json())
  .then(data => console.log(data));
```

### React/Axios Example
```javascript
import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

// Get portfolio summary
const getPortfolioSummary = async () => {
  const response = await axios.get(`${API_BASE}/portfolio/summary`);
  return response.data;
};

// Create asset
const createAsset = async (assetData) => {
  const response = await axios.post(`${API_BASE}/assets`, assetData);
  return response.data;
};
```

---

## Notes for Frontend Development

1. **Asset Types**: Currently supports "STOCK" and "CRYPTO" types
2. **Transaction Types**: Only "BUY" and "SELL" are supported
3. **Date Format**: All dates are in ISO 8601 format (e.g., "2025-01-15T10:30:00")
4. **Timeframe Options**: Common values are "1D", "1W", "1M", "3M", "6M", "1Y"
5. **Stock Symbols**: Use standard ticker symbols (e.g., AAPL, TSLA, MSFT)
6. **Dependencies**: Stock API endpoints require Flask backend to be running
