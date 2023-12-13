package com.fredsonchaves07.gamestore.web;

import com.fredsonchaves07.gamestore.domain.dtos.GameDTO;
import com.fredsonchaves07.gamestore.domain.dtos.MyGamesFinishedDTO;
import com.fredsonchaves07.gamestore.domain.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/")
    public String myGames(Model model) {
        List<MyGamesFinishedDTO> gamesFinished = gameService.getGamesFinished();
        model.addAttribute("games", gamesFinished);
        return "index";
    }
}
