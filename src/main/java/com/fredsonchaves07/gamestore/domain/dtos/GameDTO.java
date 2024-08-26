package com.fredsonchaves07.gamestore.domain.dtos;

import com.fredsonchaves07.gamestore.domain.entities.Game;
import com.fredsonchaves07.gamestore.domain.entities.Platform;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public record GameDTO (
        Integer id,
        String name,
        String url,
        Integer platformId,
        String finishedAt,
        String platformName
) {

    public GameDTO(int id, String name, String url, String platformName) {
        this(id, name, url, null, null, platformName);
    }

    public GameDTO(int id, String name, int platformId) {
        this(id, name, null, platformId, null, null);
    }

    public static GameDTO with(Game game, Platform platform) {
        String finishedAt = null;
        if (game.getFinishedAt() != null) {
            finishedAt = game.getFinishedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        String coverUrl = null;
        if (game.getCoverUrl() != null) {
            coverUrl = game.getCoverUrl().replace("t_thumb","t_cover_big");
        }
        return new GameDTO(
                game.getId(),
                game.getName(),
                coverUrl,
                game.getPlatformId(),
                finishedAt,
                platform.getName()
        );
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
                game.getFinishedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                platform.getName()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDTO gameDTO = (GameDTO) o;
        return id.equals(gameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
