CREATE TABLE IF NOT EXISTS "order" (
    id BIGSERIAL PRIMARY KEY,
    order_number TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS order_line_item (
    id BIGSERIAL PRIMARY KEY,
    sku_code TEXT NOT NULL,
    price DECIMAL NOT NULL,
    quantity INTEGER NOT NULL,
    order_id BIGINT,
    CONSTRAINT order_fk FOREIGN KEY(order_id) REFERENCES "order"(id)
);