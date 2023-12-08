package com.fredsonchaves07.gamestore;

import com.fredsonchaves07.gamestore.api.IgdbApiClient;
import com.fredsonchaves07.gamestore.domain.dtos.GameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
public class GamestoreApplication implements CommandLineRunner {

	@Autowired
	private IgdbApiClient igdbApiClient;

	public static void main(String[] args) {
		SpringApplication.run(GamestoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		ResponseEntity<GameDTO> igdbApiResponse = igdbApiClient.getGameByName("");
//		System.out.println("Obtendo jogo...");
//		System.out.println(igdbApiResponse.getBody());
	}
}
