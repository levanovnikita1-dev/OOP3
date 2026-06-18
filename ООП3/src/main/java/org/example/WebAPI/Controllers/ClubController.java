package org.example.WebAPI.Controllers;

import org.example.Class.Human;
import org.example.Class.Warrior;
import org.example.Class.Healer;
import org.example.Class.ActionLog;
import org.example.AppDataAPI.ClubService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClubController {
    private final ClubService service;
    public ClubController(ClubService service) { this.service = service; }

    @GetMapping("/heroes")
    public List<Human> getHeroes() { return service.getAllHeroes(); }

    @PostMapping("/warriors")
    public Human addWarrior(@RequestBody Warrior warrior) {
        warrior.setHeroType("Warrior");
        return service.saveHero(warrior);
    }

    @PostMapping("/healers")
    public Human addHealer(@RequestBody Healer healer) {
        healer.setHeroType("Healer");
        return service.saveHero(healer);
    }

    @GetMapping("/logs")
    public List<ActionLog> getLogs(@RequestParam(required = false) Integer heroId) {
        if (heroId != null) return service.getLogsByHeroId(heroId);
        return service.getAllLogs();
    }

    @PostMapping("/battle/skill")
    public void useSkill(@RequestParam Integer actorId, @RequestParam Integer targetId, @RequestParam String action) {
        service.handleSkillAction(actorId, targetId, action);
    }
    @DeleteMapping("/heroes/{id}")
    public void deleteHero(@PathVariable Integer id) {
        service.deleteHero(id);
    }
}