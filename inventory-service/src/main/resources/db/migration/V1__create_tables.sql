CREATE TABLE IF NOT EXISTS inventory(
    id BIGSERIAL PRIMARY KEY,
    sku_code TEXT NOT NULL,
    quantity INTEGER NOT NULL
);