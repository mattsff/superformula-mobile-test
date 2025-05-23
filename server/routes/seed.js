const express = require("express");
const { v4: uuidv4 } = require("uuid");
const dayjs = require("dayjs");

const router = express.Router();

// In-memory storage for seeds and their expiration
const seedStorage = new Map(); // seed -> expiration (Date object)

// Helper: remove expired seeds
function cleanExpiredSeeds() {
  const now = dayjs();
  for (const [seed, expiration] of seedStorage.entries()) {
    if (now.isAfter(expiration)) {
      seedStorage.delete(seed);
    }
  }
}

// GET /seed - generate a new seed
router.get("/", (req, res) => {
  cleanExpiredSeeds();
  const seed = uuidv4().replace(/-/g, "");
  const expiration = dayjs().add(1, "minute").toDate();
  seedStorage.set(seed, expiration);

  // Log new seed
  console.log(
    `[seed] Generated: ${seed} (expires at ${expiration.toISOString()})`
  );

  res.status(200).json({ seed, expires_at: expiration.getTime() });
});

// GET /validate?seed=... - validate a seed
router.get("/validate", (req, res) => {
  const seed = req.query.seed;
  if (!seed) {
    console.log("[validate] Missing seed");
    return res.status(200).json({ valid: false, reason: "Missing seed" });
  }
  const expiration = seedStorage.get(seed);
  if (!expiration) {
    console.log(`[validate] Not found: ${seed}`);
    return res.status(200).json({ valid: false, reason: "Seed not found" });
  }
  if (dayjs().isAfter(expiration)) {
    console.log(`[validate] Expired: ${seed}`);
    seedStorage.delete(seed);
    return res.status(200).json({ valid: false, reason: "Seed expired" });
  }
  console.log(`[validate] Valid: ${seed}`);
  return res.json({ valid: true });
});

module.exports = router;
