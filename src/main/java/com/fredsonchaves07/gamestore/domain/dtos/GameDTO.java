package com.fredsonchaves07.gamestore.domain.dtos;

import com.fredsonchaves07.gamestore.domain.entities.Game;
import com.fredsonchaves07.gamestore.domain.entities.Platform;

public record GameDTO (
        Integer id,
        String name,
        String url,
        Integer platformId,
        String platformName
) {

    public GameDTO(int id, String name, String url, String platformName) {
        this(id, name, url, null, platformName);
    }

    public GameDTO(int id, String name, int platformId) {
        this(id, name, null, platformId, null);
    }

    public static GameDTO from(Game game) {
        Platform platform = game
                .getPlatforms()
                .stream()
                .filter(platformGame -> platformGame.getId() == game.getPlatformId())
                .findFirst().orElseThrow();
        return new GameDTO(
                game.getId(),
                game.getName(),
                game.getCoverUrl(),
                game.getPlatformId(),
                platform.getName()
        );
    }
}
