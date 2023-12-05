package com.fredsonchaves07.gamestore;

import org.apache.juli.logging.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import java.util.logging.Logger;

@SpringBootApplication
public class GamestoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GamestoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		ResponseEntity<String> igdbApiClient = new IgdbApiClient().obterDadosJogo("");
//		System.out.println("Obtendo jogo...");
//		System.out.println(igdbApiClient.getBody());
	}
}
