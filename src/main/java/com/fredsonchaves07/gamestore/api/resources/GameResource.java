package com.fredsonchaves07.gamestore.api.resources;

import com.fredsonchaves07.gamestore.domain.dtos.GameDTO;
import com.fredsonchaves07.gamestore.domain.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
public class GameResource {

    @Autowired
    private GameService gameService;

    @GetMapping(value = "/draw")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<GameDTO> draw() throws InterruptedException {
        GameDTO gameDTO = gameService.draw();
        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }
}
