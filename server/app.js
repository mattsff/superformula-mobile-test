const express = require("express");
const seedRouter = require("./routes/seed");

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());
app.use("/seed", seedRouter);

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});

module.exports = app;
