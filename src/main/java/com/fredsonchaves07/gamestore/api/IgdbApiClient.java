package com.fredsonchaves07.gamestore.api;

import com.fredsonchaves07.gamestore.domain.dtos.GameDTO;
import com.fredsonchaves07.gamestore.domain.entities.Game;
import com.fredsonchaves07.gamestore.domain.entities.Platform;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class IgdbApiClient {

    @Value("${gamestore.client.id}")
    private String clientID;

    @Value("${gamestore.acess.token}")
    private String acessToken;

    @Value("${api.url.database.games}")
    private String apiGamesUrl;

    @Value("${api.url.database.platforms}")
    private String apiPlatformsUrl;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientID);
        headers.set("Authorization", "Bearer " + acessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public int getCountGamesByPlatform(int plataform_id) {
        String query = "where platforms = " + plataform_id + ";";
        HttpEntity<String> entity = new HttpEntity<>(query, getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                apiGamesUrl + "/count",
                HttpMethod.POST,
                entity,
                String.class
        );
        return JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject().get("count").getAsInt();
    }

    public int getCountGamesByListGamesIdAndPlatformId(List<Integer> gamesId,  int platformId) {
        String query = String.format("""
                    where id = %s & platforms = (%d);
                   """,  gamesId.toString().replace("[", "(").replace("]", ")"), platformId);
        HttpEntity<String> entity = new HttpEntity<>(query, getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                apiGamesUrl + "/count",
                HttpMethod.POST,
                entity,
                String.class
        );
        return JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject().get("count").getAsInt();
    }

    //TODO -> Refatorar para retornar Game ao invés de GameDTO
    public List<GameDTO> getGameByPlatform(int plataform_id, int offset, String platformName) {
        List<GameDTO> games = new ArrayList<>();
        String query = String.format("""
                    fields id, name, url;
                    where platforms = %d;
                    limit 500;
                    offset %d;""",  plataform_id, offset);
        HttpEntity<String> entity = new HttpEntity<>(query, getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                apiGamesUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        for (JsonElement element : JsonParser.parseString(response.getBody()).getAsJsonArray()) {
            games.add(
                    new GameDTO(
                            element.getAsJsonObject().get("id").getAsInt(),
                            element.getAsJsonObject().get("name").getAsString(),
                            element.getAsJsonObject().get("url").getAsString(),
                            platformName
                    )
            );
        }
       return games;
    }

    public ResponseEntity<String> getGameByPlatform(int plataform_id) {
        String query = String.format("""
                    fields id, name, url;
                    where platforms = %d;
                    limit 1;
                    offset 1;""",  plataform_id);
        HttpEntity<String> entity = new HttpEntity<>(query, getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                apiGamesUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }

    public Game getGameByIdAndPlatformId(int gameId, int platformId) {
        Game game = null;
        String query = String.format("""
                    fields id, name, cover.url;
                    where id = %d & platforms.id = %d;
                   """,  gameId, platformId);
        HttpEntity<String> entity = new HttpEntity<>(query, getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                apiGamesUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        for (JsonElement element : JsonParser.parseString(response.getBody()).getAsJsonArray()) {
            game =
                    new Game(
                            element.getAsJsonObject().get("id").getAsInt(),
                            element.getAsJsonObject().get("name").getAsString(),
                            element.getAsJsonObject().get("cover").getAsJsonObject().get("url").getAsString()
                    );
        }
        return game;
    }

    public GameDTO getGameByName(String name, String platformName) {
        GameDTO gameDTO = null;
        String query = String.format("""
                    fields id, name, url;
                    where name = "%s";
                    limit 1;""",  name);
        HttpEntity<String> entity = new HttpEntity<>(query, getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                apiGamesUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        for (JsonElement element : JsonParser.parseString(response.getBody()).getAsJsonArray()) {
            gameDTO =
                    new GameDTO(
                            element.getAsJsonObject().get("id").getAsInt(),
                            element.getAsJsonObject().get("name").getAsString(),
                            element.getAsJsonObject().get("url").getAsString(),
                            platformName
                    );
        }
        return gameDTO;
    }

    public Game getGameById(int gameId) {
        Game game = null;
        String query = String.format("""
                    fields id, name, cover.url;
                    where id = %d;
        """, gameId);
        HttpEntity<String> entity = new HttpEntity<>(query, getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                apiGamesUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        for (JsonElement element : JsonParser.parseString(response.getBody()).getAsJsonArray()) {
            game =
                    new Game(
                            element.getAsJsonObject().get("id").getAsInt(),
                            element.getAsJsonObject().get("name").getAsString(),
                            element.getAsJsonObject().get("cover").getAsJsonObject().get("url").getAsString()
                    );
        }
        return game;
    }

    public Platform getPlatformById(int platformId) {
        Platform platform = null;
        String query = String.format("""
                    fields id, name;
                    where id = %d;
        """, platformId);
        HttpEntity<String> entity = new HttpEntity<>(query, getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                apiPlatformsUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        for (JsonElement element : JsonParser.parseString(response.getBody()).getAsJsonArray()) {
            platform = new Platform(
                    element.getAsJsonObject().get("id").getAsInt(),
                    element.getAsJsonObject().get("name").getAsString()
            );
        }
        return platform;
    }

    public Set<Integer> getPlatformsIdByGameId(int gameId) {
        Set<Integer> platformsId = new HashSet<>();
        String query = String.format("""
                    fields platforms.id;
                    where id = %d;
        """, gameId);
        HttpEntity<String> entity = new HttpEntity<>(query, getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                apiGamesUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        for (JsonElement element : JsonParser.parseString(response.getBody()).getAsJsonArray()) {
            List<JsonElement> jsonElements = element.getAsJsonObject().get("platforms").getAsJsonArray().asList();
            for (JsonElement jsonElement : jsonElements) {
                platformsId.add(jsonElement.getAsJsonObject().get("id").getAsInt());
            }
        }
        return platformsId;
    }
}