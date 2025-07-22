CREATE TABLE IF NOT EXISTS "item"
(
    id              SERIAL PRIMARY KEY NOT NULL,

    type            VARCHAR(64) NOT NULL,
    quantity        FLOAT NOT NULL
);
