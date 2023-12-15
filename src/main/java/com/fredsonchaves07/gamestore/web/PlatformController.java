package com.fredsonchaves07.gamestore.web;

import com.fredsonchaves07.gamestore.domain.dtos.PlatformDTO;
import com.fredsonchaves07.gamestore.domain.services.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping("/platforms")
    public String myPlatforms(Model model) {
        List<PlatformDTO> platformsActive = platformService.getPlatformsActive();
        model.addAttribute("platforms", platformsActive);
        return "platforms";
    }
}
