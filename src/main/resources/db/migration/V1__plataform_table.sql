CREATE TABLE IF NOT EXISTS platforms
(
    id                      int primary key,
    alternative_name        varchar(255),
    name                    varchar(255),
    is_active               boolean DEFAULT FALSE,
    created_at              TIMESTAMP DEFAULT now(),
    updated_at              TIMESTAMP DEFAULT now()
)