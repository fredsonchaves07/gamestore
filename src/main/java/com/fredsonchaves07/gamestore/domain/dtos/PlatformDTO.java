package com.fredsonchaves07.gamestore.domain.dtos;

import com.fredsonchaves07.gamestore.domain.entities.Platform;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record PlatformDTO(
        Integer id,
        String name,
        String logoUrl,
        int gamesCount,
        int gamesFinished,
        double progress,
        boolean isActive,
        boolean isLastChosen
) {

    public static PlatformDTO with(Platform platform, int gamesCount, int gamesFinished) {
        double progress = BigDecimal.valueOf((Double.valueOf(gamesFinished) / gamesCount) * 100).setScale(2, RoundingMode.DOWN).doubleValue();
        return new PlatformDTO(
                platform.getId(),
                platform.getName(),
                platform.getLogoUrl(),
                gamesCount,
                gamesFinished,
                progress,
                platform.isActive(),
                platform.isLastChosen()
        );
    }

    public static PlatformDTO from(Platform platform) {
        return new PlatformDTO(
                platform.getId(),
                platform.getName(),
                platform.getLogoUrl(),
                0,
                0,
                0.0,
                platform.isActive(),
                platform.isLastChosen()
        );
    }
}
