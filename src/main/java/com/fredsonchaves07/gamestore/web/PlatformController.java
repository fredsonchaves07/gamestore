package com.fredsonchaves07.gamestore.web;

import com.fredsonchaves07.gamestore.domain.dtos.GameDTO;
import com.fredsonchaves07.gamestore.domain.dtos.PlatformDTO;
import com.fredsonchaves07.gamestore.domain.services.GameService;
import com.fredsonchaves07.gamestore.domain.services.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private GameService gameService;

    @GetMapping("/platforms")
    public String getPlatforms(Model model) {
        List<PlatformDTO> platformsActive = platformService.getPlatformsActive();
        model.addAttribute("platforms", platformsActive);
        return "platforms";
    }

    @GetMapping("/platforms/{id}")
    public String getPlatformsById(@PathVariable Integer id, Model model) {
        PlatformDTO platform = platformService.getPlatformById(id);
        List<GameDTO> games = gameService.getGamesByPlatformId(id);
        model.addAttribute("platform", platform);
        model.addAttribute("games", games);
        return "detail-platform";
    }

    @GetMapping("/platforms/{id}/import")
    public String importGamesByPlatform(@PathVariable Integer id, Model model) {
        gameService.importGamesByPlatformfId(id);
        return getPlatforms(model);
    }
}
