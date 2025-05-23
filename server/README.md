# QR API Server

## 📦 Requirements

- Node.js v18+
- npm

## 🚀 Start Server

```bash
npm install
npm run start
```

## 🚀 Test

```bash
npm install
npm run test
```

## 📡 How to test the APIs

With the server running, you can test the endpoints using tools like `curl`, Postman, or your browser.

### Get a seed

Endpoint: `GET /seed`

Example using curl:

```bash
curl http://localhost:3000/seed
```

Expected response:

```json
{
  "seed": "<uuid-value>",
  "expires_at": "<iso-expiration-date>"
}
```

- `seed`: unique identifier generated.
- `expires_at`: expiration date and time for the seed (1 minute from generation).
