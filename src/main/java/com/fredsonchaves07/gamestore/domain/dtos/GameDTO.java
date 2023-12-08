package com.fredsonchaves07.gamestore.domain.dtos;

public record GameDTO (
        int id,
        String name,
        String url,
        String platformName
) {

}
