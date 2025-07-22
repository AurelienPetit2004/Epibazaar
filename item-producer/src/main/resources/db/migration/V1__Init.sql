CREATE TABLE IF NOT EXISTS "game"
(
    id                              SERIAL PRIMARY KEY NOT NULL,
    map                             TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "item"
(
    id                              SERIAL PRIMARY KEY NOT NULL,

    type                            VARCHAR(64) NOT NULL,
    quantity                        FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS "player"
(
    id                              SERIAL PRIMARY KEY NOT NULL,
    pos_x                           INTEGER NOT NULL,
    pos_y                           INTEGER NOT NULL,
    last_move                       TIMESTAMP,
    last_collect                    TIMESTAMP,
    move_speed_multiplier           FLOAT NOT NULL,
    stamina_multiplier              FLOAT NOT NULL,
    collect_rate_multiplier         FLOAT NOT NULL
);
