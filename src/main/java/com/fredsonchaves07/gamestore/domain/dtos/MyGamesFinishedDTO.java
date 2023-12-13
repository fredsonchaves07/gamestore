package com.fredsonchaves07.gamestore.domain.dtos;

import com.fredsonchaves07.gamestore.domain.entities.Game;
import com.fredsonchaves07.gamestore.domain.entities.Platform;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record MyGamesFinishedDTO(
        Integer id,
        String name,
        Integer platformId,
        String platformName,
        String coverUrl,
        String finishCondition,
        Integer rating,
        boolean isGold,
        boolean isPlatina,
        String finishedAt,
        String finishTime
) {

    public static MyGamesFinishedDTO from(Game game) {
        Platform platform = game
                .getPlatforms()
                .stream()
                .filter(platformGame -> platformGame.getId() == game.getPlatformId())
                .findFirst().orElseThrow();
        return new MyGamesFinishedDTO(
                game.getId(),
                game.getName(),
                game.getPlatformId(),
                platform.getName(),
                game.getCoverUrl(),
                game.getFinishCondition(),
                game.getRating(),
                game.isGold(),
                game.isPlatina(),
                game.getFinishedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                LocalTime.ofSecondOfDay(game.getFinishTime()).toString()
        );
    }
}
