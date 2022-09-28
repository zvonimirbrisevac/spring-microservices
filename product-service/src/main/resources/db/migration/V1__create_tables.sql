CREATE TABLE IF NOT EXISTS product (
    id  BIGSERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    price DECIMAL
)