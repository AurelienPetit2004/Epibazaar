CREATE TABLE IF NOT EXISTS "shop"
(
    id                          SERIAL PRIMARY KEY NOT NULL,

    price_multiplier            FLOAT NOT NULL,
    upgrade_price               FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS "item"
(
    id                          SERIAL PRIMARY KEY NOT NULL,

    type                        VARCHAR(64) NOT NULL,
    quantity                    FLOAT NOT NULL
);
