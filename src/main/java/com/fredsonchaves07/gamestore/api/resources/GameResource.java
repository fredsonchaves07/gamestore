package com.fredsonchaves07.gamestore.api.resources;

import com.fredsonchaves07.gamestore.domain.dtos.FinishGameDTO;
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

    @GetMapping(value = "/draw/{platformId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<GameDTO> drawByPlatform(@PathVariable int platformId) throws InterruptedException {
        GameDTO gameDTO = gameService.drawByPlatform(platformId);
        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @PostMapping(value = "/finish")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<GameDTO> finishGame(@RequestBody FinishGameDTO finishGameDTO) throws InterruptedException {
        GameDTO gameDTO = gameService.finish(finishGameDTO);
        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }
}
