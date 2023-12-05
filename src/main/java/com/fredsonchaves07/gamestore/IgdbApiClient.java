package com.fredsonchaves07.gamestore;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class IgdbApiClient {

//    @Value("${gamestore.client.id}")
    private String clientID;

//    @Value("${gamestore.token}")
    private String token;

    private final String apiUrl = "https://api.igdb.com/v4/games";


    public ResponseEntity<String> obterDadosJogo(String nomeJogo) {
        // Configurar cabeçalhos com a chave da API
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientID);
        headers.set("Authorization", "Bearer " + token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Configurar parâmetros da solicitação
        String params = "search=" + nomeJogo + "&fields=name,summary,cover";

        // Configurar a URL completa
        String url = apiUrl + "?" + params;

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        // Fazer a solicitação GET
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response;
    }
}