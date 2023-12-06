CREATE TABLE IF NOT EXISTS games_platforms
(
    platform_id                     int REFERENCES platforms (id) ON UPDATE CASCADE ON DELETE CASCADE,
    game_id                         int REFERENCES games (id) ON UPDATE CASCADE ON DELETE CASCADE,
    created_at                      TIMESTAMP DEFAULT now(),
    updated_at                      TIMESTAMP DEFAULT now(),
    CONSTRAINT games_platforms_pkey PRIMARY KEY (platform_id, game_id)
);