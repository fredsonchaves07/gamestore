package com.fredsonchaves07.gamestore.domain.services;

import com.fredsonchaves07.gamestore.api.IgdbApiClient;
import com.fredsonchaves07.gamestore.domain.dtos.PlatformDTO;
import com.fredsonchaves07.gamestore.domain.entities.Game;
import com.fredsonchaves07.gamestore.domain.entities.Platform;
import com.fredsonchaves07.gamestore.domain.repositories.GameRepository;
import com.fredsonchaves07.gamestore.domain.repositories.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private IgdbApiClient igdbApiClient;

    public List<PlatformDTO> getPlatformsActive() {
        List<PlatformDTO> allPlatformsDTOActive = new ArrayList<>();
        List<Platform> allPlatformsActive = platformRepository.findAllPlatformsActive();
        List<Integer> gamesIdFinished = gameRepository.findAllGamesFinished().stream().map(Game::getId).toList();
        for (Platform platform : allPlatformsActive) {
            int count = igdbApiClient.getCountGamesByPlatform(platform.getId());
            int gamesFinished = igdbApiClient.getCountGamesByListGamesIdAndPlatformId(gamesIdFinished, platform.getId());
            allPlatformsDTOActive.add(PlatformDTO.with(platform, count, gamesFinished));
        }
        return allPlatformsDTOActive;
    }
}
