CREATE TABLE IF NOT EXISTS games
(
    id                      int primary key,
    name                    varchar(255),
    rating                  int,
    platform_id             int,
    is_gold                 boolean DEFAULT FALSE,
    is_platina              boolean DEFAULT FALSE,
    finished_at             TIMESTAMP,
    created_at              TIMESTAMP DEFAULT now(),
    updated_at              TIMESTAMP DEFAULT now()
);

