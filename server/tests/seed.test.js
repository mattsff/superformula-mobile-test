const request = require("supertest");
const app = require("../app");

describe("GET /seed", () => {
  it("should return a seed and an expiration date", async () => {
    const res = await request(app).get("/seed");

    expect(res.statusCode).toEqual(200);
    expect(res.body).toHaveProperty("seed");
    expect(res.body).toHaveProperty("expires_at");
    expect(typeof res.body.seed).toBe("string");
    expect(new Date(res.body.expires_at).toString()).not.toBe("Invalid Date");
  });
});

describe("GET /validate", () => {
  it("should validate a valid seed", async () => {
    const resSeed = await request(app).get("/seed");
    const seed = resSeed.body.seed;
    const res = await request(app).get(`/seed/validate?seed=${seed}`);
    expect(res.statusCode).toBe(200);
    expect(res.body).toEqual({ valid: true });
  });

  it("should return not found for an invalid seed", async () => {
    const res = await request(app).get(`/seed/validate?seed=invalidseed123`);
    expect(res.statusCode).toBe(200);
    expect(res.body).toEqual({ valid: false, reason: "Seed not found" });
  });

  it("should return expired for an expired seed", async () => {
    // Create a seed and manually expire it
    const resSeed = await request(app).get("/seed");
    const seed = resSeed.body.seed;
    // Simulate expiration by waiting 61 seconds
    jest.useFakeTimers();
    jest.setSystemTime(Date.now() + 61 * 1000);
    const res = await request(app).get(`/seed/validate?seed=${seed}`);
    expect(res.statusCode).toBe(200);
    expect(res.body).toEqual({ valid: false, reason: "Seed expired" });
    jest.useRealTimers();
  });

  it("should return missing seed if no seed param", async () => {
    const res = await request(app).get(`/seed/validate`);
    expect(res.statusCode).toBe(200);
    expect(res.body).toEqual({ valid: false, reason: "Missing seed" });
  });
});
