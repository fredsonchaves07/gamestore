package com.fredsonchaves07.gamestore.domain.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public record FinishGameDTO(
        int id,
        LocalTime finishTime,
        String finishCondition,
        int rating,
        boolean isGold,
        boolean isPlatina,
        int platformId,
        LocalDate finishedAt
) {
}
