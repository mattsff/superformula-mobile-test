# QR API Server

A simple Node.js backend for generating and validating QR code seeds, designed to work with mobile or web clients.

## 📦 Requirements

- Node.js v18+
- npm

## 🚀 Start Server

```bash
npm install
npm run start
```

The server will run by default on [http://localhost:3000](http://localhost:3000).

## 🚀 Test

Run all tests with:

```bash
npm run test
```

Test results and coverage will be shown in the terminal.

## 📡 API Reference

### Generate a Seed

- **Endpoint:** `GET /seed`
- **Description:** Generates a new unique seed and returns its expiration timestamp (1 minute from creation).
- **Example:**

  ```bash
  curl http://localhost:3000/seed
  ```

- **Response:**

  ```json
  {
    "seed": "<uuid-value>",
    "expires_at": <timestamp>
  }
  ```

  - `seed`: Unique identifier generated.
  - `expires_at`: Expiration time in milliseconds since epoch.

### Validate a Seed

- **Endpoint:** `GET /seed/validate?seed=<seed>`
- **Description:** Checks if a seed is valid, expired, or not found.
- **Example:**

  ```bash
  curl "http://localhost:3000/seed/validate?seed=YOUR_SEED_HERE"
  ```

- **Response:**
  - Valid:
    ```json
    { "valid": true }
    ```
  - Invalid:
    ```json
    { "valid": false, "reason": "Seed not found" }
    ```
  - Expired:
    ```json
    { "valid": false, "reason": "Seed expired" }
    ```
