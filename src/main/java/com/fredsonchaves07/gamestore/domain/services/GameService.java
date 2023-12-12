package com.fredsonchaves07.gamestore.domain.services;

import com.fredsonchaves07.gamestore.api.IgdbApiClient;
import com.fredsonchaves07.gamestore.domain.dtos.FinishGameDTO;
import com.fredsonchaves07.gamestore.domain.dtos.GameDTO;
import com.fredsonchaves07.gamestore.domain.entities.Game;
import com.fredsonchaves07.gamestore.domain.entities.Platform;
import com.fredsonchaves07.gamestore.domain.repositories.GameRepository;
import com.fredsonchaves07.gamestore.domain.repositories.PlatformRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private IgdbApiClient igdbApiClient;

    @Autowired
    private ResourceLoader resourceLoader;

    @Transactional
    public GameDTO draw() throws InterruptedException {
        Platform platform = drawPlatform();
        Thread.sleep(2000);
        GameDTO game = drawGameByPlatform(platform);
        platform.setLastChosen(true);
        return game;
    }

    @Transactional
    public GameDTO drawByPlatform(int platformId) throws InterruptedException {
        Platform platform = platformRepository.findById(platformId).get();
        if (!platform.isLastChosen()) throw new Error("Please draw game by /draw");
        GameDTO game = drawGameByPlatform(platform);
        platform.setLastChosen(true);
        return game;
    }

    private Platform drawPlatform() throws InterruptedException {
        Thread.sleep(1000);
        Platform excludePlataform = getExcludePlatformInDraw();
        List<Platform> platforms = platformRepository.findAllPlatformsActive();
        Collections.shuffle(platforms);
        platforms.remove(excludePlataform);
        Map<Platform, Integer> platformPoints = new HashMap<>();
        platforms.forEach(platform -> platformPoints.put(platform, 0));
        Object[] plataformArray = platformPoints.keySet().toArray();
        for (int i = 1; i <= 5; i ++) {
            for (Map.Entry<Platform, Integer> platform : platformPoints.entrySet()) {
                if (platform.getValue() == 3) {
                    return platform.getKey();
                }
            }
            Random random = new Random();
            int index = random.nextInt(platformPoints.size());
            Platform platformSelected = (Platform) plataformArray[index];
            Integer platformSelectedPoint = platformPoints.get(platformSelected);
            platformPoints.put(platformSelected, platformSelectedPoint + 1);
        }
        int max = Integer.MIN_VALUE;
        for (int value : platformPoints.values()) {
            if (value > max) {
                max = value;
            }
        }
        Map<Platform, Integer> plataformSelectedList = new HashMap<>();
        for (Map.Entry<Platform, Integer> platform : platformPoints.entrySet()) {
            if (platform.getValue() == max) {
                plataformSelectedList.put(platform.getKey(), platform.getValue());
            }
        }
        if (plataformSelectedList.size() > 1) {
            Random random = new Random();
            int index = random.nextInt(plataformSelectedList.size());
            plataformArray = plataformSelectedList.keySet().toArray();
            Platform platformSelected = (Platform) plataformArray[index];
            Integer platformSelectedPoint = platformPoints.get(platformSelected);
            platformPoints.put(platformSelected, platformSelectedPoint + 1);
            max = Integer.MIN_VALUE;
            for (int value : platformPoints.values()) {
                if (value > max) {
                    max = value;
                }
            }
            plataformSelectedList = new HashMap<>();
            for (Map.Entry<Platform, Integer> platform : platformPoints.entrySet()) {
                if (platform.getValue() == max) {
                    plataformSelectedList.put(platform.getKey(), platform.getValue());
                }
            }
        }
        Platform selectedPlataform = (Platform) plataformSelectedList.keySet().toArray()[0];
        return selectedPlataform;
    }

    private Platform getExcludePlatformInDraw() {
        Map<Platform, Integer> platformPoints = new HashMap<>();
        List<Platform> platforms = platformRepository.findAllPlatformsActive();
        Collections.shuffle(platforms);
        platforms.forEach(platform -> platformPoints.put(platform, 0));
        List<Platform> platformLastChosenList = platformRepository.getPlatformsLastChosen();
        platformLastChosenList.forEach(platform -> platform.setLastChosen(false));
        platformLastChosenList.forEach(platform -> platformPoints.put(platform, 1));
        Object[] plataformArray = platformPoints.keySet().toArray();
        for (int i = 1; i <= 5; i ++) {
            for (Map.Entry<Platform, Integer> platform : platformPoints.entrySet()) {
                if (platform.getValue() == 3) {
                    return platform.getKey();
                }
            }
            Random random = new Random();
            int index = random.nextInt(platformPoints.size());
            Platform platformSelected = (Platform) plataformArray[index];
            Integer platformSelectedPoint = platformPoints.get(platformSelected);
            platformPoints.put(platformSelected, platformSelectedPoint + 1);
        }
        int max = Integer.MIN_VALUE;
        for (int value : platformPoints.values()) {
            if (value > max) {
                max = value;
            }
        }
        Map<Platform, Integer> plataformExcludeSelectedList = new HashMap<>();
        for (Map.Entry<Platform, Integer> platform : platformPoints.entrySet()) {
            if (platform.getValue() == max) {
                plataformExcludeSelectedList.put(platform.getKey(), platform.getValue());
            }
        }
        if (plataformExcludeSelectedList.size() > 1) {
            Random random = new Random();
            int index = random.nextInt(plataformExcludeSelectedList.size());
            plataformArray = plataformExcludeSelectedList.keySet().toArray();
            Platform platformSelected = (Platform) plataformArray[index];
            Integer platformSelectedPoint = platformPoints.get(platformSelected);
            platformPoints.put(platformSelected, platformSelectedPoint + 1);
            max = Integer.MIN_VALUE;
            for (int value : platformPoints.values()) {
                if (value > max) {
                    max = value;
                }
            }
            plataformExcludeSelectedList = new HashMap<>();
            for (Map.Entry<Platform, Integer> platform : platformPoints.entrySet()) {
                if (platform.getValue() == max) {
                    plataformExcludeSelectedList.put(platform.getKey(), platform.getValue());
                }
            }
        }
        Platform excludePlatform = (Platform) plataformExcludeSelectedList.keySet().toArray()[0];
        return excludePlatform;
    }

    private GameDTO drawGameByPlatform(Platform platform) throws InterruptedException {
        List<GameDTO> gameList = new ArrayList<>();
        List<GameDTO> gamesFinished = gameRepository.findAllGamesFinished().stream().map(
                game -> new GameDTO(game.getId(), game.getName(), platform.getId())
        ).toList();
        if (isSelectGamePlatformByFile(platform)) {
            gameList = getGamesByFile(platform);
        } else {
            int gameCount = igdbApiClient.getCountGamesByPlatform(platform.getId());
            int limit = (gameCount / 500) + 1;
            int ofset = 0;
            for (int i = 1; i <= limit; i ++) {
                List<GameDTO> gameApiList = igdbApiClient.getGameByPlatform(platform.getId(), ofset, platform.getName());
                for (GameDTO game : gameApiList) {
                    if (!gameList.contains(game) && !gamesFinished.contains(game)) {
                        gameList.add(game);
                    }
                }
                if (gameList.size() == gameCount) {
                    break;
                }
                ofset = ofset + 500;
            }
        }
        Collections.shuffle(gameList);
        Map<GameDTO, Integer> gamePoints = new HashMap<>();
        gameList.forEach(gameDTO -> gamePoints.put(gameDTO, 0));
        Object[] gameArray = gamePoints.keySet().toArray();
        for (int i = 1; i <= 5; i ++) {
            for (Map.Entry<GameDTO, Integer> game : gamePoints.entrySet()) {
                if (game.getValue() == 3) {
                    return game.getKey();
                }
            }
            Random random = new Random();
            int index = random.nextInt(gamePoints.size());
            GameDTO gameSelected = (GameDTO) gameArray[index];
            Integer platformSelectedPoint = gamePoints.get(gameSelected);
            gamePoints.put(gameSelected, platformSelectedPoint + 1);
        }
        int max = Integer.MIN_VALUE;
        for (int value : gamePoints.values()) {
            if (value > max) {
                max = value;
            }
        }
        Map<GameDTO, Integer> gameSelectedList = new HashMap<>();
        for (Map.Entry<GameDTO, Integer> game : gamePoints.entrySet()) {
            if (game.getValue() == max) {
                gameSelectedList.put(game.getKey(), game.getValue());
            }
        }
        if (gameSelectedList.size() > 1) {
            Random random = new Random();
            int index = random.nextInt(gameSelectedList.size());
            gameArray = gameSelectedList.keySet().toArray();
            GameDTO gameSelected = (GameDTO) gameArray[index];
            Integer gameSelectedPoint = gamePoints.get(gameSelected);
            gamePoints.put(gameSelected, gameSelectedPoint + 1);
            max = Integer.MIN_VALUE;
            for (int value : gamePoints.values()) {
                if (value > max) {
                    max = value;
                }
            }
            gameSelectedList = new HashMap<>();
            for (Map.Entry<GameDTO, Integer> game : gamePoints.entrySet()) {
                if (game.getValue() == max) {
                    gameSelectedList.put(game.getKey(), game.getValue());
                }
            }
        }
        GameDTO selectedGame = (GameDTO) gameSelectedList.keySet().toArray()[0];
        return selectedGame;
    }

    private boolean isSelectGamePlatformByFile(Platform platform) {
        return platform.getId().equals(48) || platform.getId().equals(6);
    }

    private List<GameDTO> getGamesByFile(Platform platform) {
        List<String> gameNameList = new ArrayList<>();
        List<GameDTO> gameDTOList = new ArrayList<>();
        List<GameDTO> gamesFinished = gameRepository.findAllGamesFinished().stream().map(
                game -> new GameDTO(game.getId(), game.getName(), platform.getId())
        ).toList();
        Resource resource = null;
        if (platform.getId().equals(48)) {
            resource = resourceLoader.getResource("classpath:" + "playstation.txt");
        } else if (platform.getId().equals(6)) {
            resource = resourceLoader.getResource("classpath:" + "pc.txt");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                gameNameList.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String name : gameNameList) {
            gameDTOList.add(igdbApiClient.getGameByName(name, platform.getName()));
        }
        gameDTOList.removeIf(gamesFinished::contains);
        return gameDTOList;
    }

    @Transactional
    public GameDTO finish(FinishGameDTO finishGameDTO) {
        int gameId = finishGameDTO.id();
        Game game = gameRepository.findById(gameId).orElse(igdbApiClient.getGameById(gameId));
        Set<Integer> platformsId = igdbApiClient.getPlatformsIdByGameId(gameId);
        for (Integer platformId : platformsId) {
            platformRepository.findById(platformId).ifPresent(game::addPlatform);
        }
        game.setFinishTime(finishGameDTO.finishTime().toSecondOfDay());
        game.setFinishCondition(finishGameDTO.finishCondition());
        game.setRating(finishGameDTO.rating());
        game.setGold(finishGameDTO.isGold());
        game.setPlatina(finishGameDTO.isPlatina());
        game.setPlatformId(finishGameDTO.platformId());
        game.setFinishedAt(finishGameDTO.finishedAt());
        gameRepository.save(game);
        return GameDTO.from(game);
    }
}
