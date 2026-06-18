package org.example.AppDataAPI;

import org.example.Class.Human;
import org.example.Class.Warrior;
import org.example.Class.Healer;
import org.example.Class.ActionLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClubService {

    private final HumanRepository humanRepository;
    private final ActionLogRepository actionLogRepository;

    public ClubService(HumanRepository humanRepository, ActionLogRepository actionLogRepository) {
        this.humanRepository = humanRepository;
        this.actionLogRepository = actionLogRepository;
    }

    public List<Human> getAllHeroes() { 
        return humanRepository.findAll(); 
    }
    
    public Human saveHero(Human hero) { 
        return humanRepository.save(hero); 
    }
    
    // Получение всех логов без кастомных сортировок
    public List<ActionLog> getAllLogs() {
        return actionLogRepository.findAll();
    }

    // Чистый поиск логов по ID героя через связь (без фильтрации и сортировки)
    public List<ActionLog> getLogsByHeroId(Integer heroId) {
        Human hero = humanRepository.findById(heroId).orElse(null);
        if (hero == null) return java.util.Collections.emptyList();
        return hero.getActionLogs();
    }

    // МЕТОД УДАЛЕНИЯ, КОТОРЫЙ ИСКАЛ КОНТРОЛЛЕР
    @Transactional
    public void deleteHero(Integer heroId) {
        if (humanRepository.existsById(heroId)) {
            humanRepository.deleteById(heroId);
        }
    }

    @Transactional
    public void handleSkillAction(Integer actorId, Integer targetId, String action) {
        Human actor = humanRepository.findById(actorId).orElseThrow(() -> new RuntimeException("Герой не найден"));
        Human target = targetId != 0 ? humanRepository.findById(targetId).orElse(null) : null;

        if (actor.getHp() == 0) {
            saveBattleLog(actor, String.format("Павший боец %s не может совершать ходы!", actor.getName()));
            return;
        }

        switch (action) {
            case "attack": executeWarriorAttack(actor, target); break;
            case "buff":
                actor.setPosition(2); humanRepository.save(actor);
                saveBattleLog(actor, String.format("🛡️ Воин %s приготовился отразить удар!", actor.getName()));
                break;
            case "block":
                actor.setPosition(1); humanRepository.save(actor);
                saveBattleLog(actor, String.format("🧱 Воин %s закрылся щитом.", actor.getName()));
                break;
            case "heal": executeHealerHeal(actor, target, false); break;
            case "resurrect": executeHealerHeal(actor, target, true); break;
            case "magicAttack": executeHealerMagicAttack(actor, target); break;
        }
    }

    private void executeWarriorAttack(Human attacker, Human target) {
        if (target == null || target.getHp() == 0) return;
        Warrior warrior = (Warrior) attacker;
        int finalDamage = warrior.getDamage();
        String msg = "";

        if (warrior.getHasBuff() != null && warrior.getHasBuff()) {
            finalDamage = (int) (finalDamage * 1.5);
            msg = String.format("⚔️ Воин %s наносит КРИТИЧЕСКИЙ урон оружием '%s' по %s! Урон: %d.", warrior.getName(), warrior.getWeapon(), target.getName(), finalDamage);
        } else {
            msg = String.format("⚔️ Воин %s наносит удар оружием '%s' по %s! Исходный урон: %d.", warrior.getName(), warrior.getWeapon(), target.getName(), finalDamage);
        }

        if (target.getPosition() != null && target.getPosition() == 1) {
            finalDamage /= 2; target.setPosition(0);
        }

        int newTargetHp = Math.max(0, target.getHp() - finalDamage);
        target.setHp(newTargetHp); humanRepository.save(target);
        saveBattleLog(warrior, msg);
    }

    private void executeHealerHeal(Human healerObj, Human target, boolean isResurrectAction) {
        if (target == null) return;
        Healer healer = (Healer) healerObj;
        int amt = healer.getHealPower();

        if (target.getHp() == 0) {
            if (isResurrectAction && healer.getCanResurrect() != null && healer.getCanResurrect()) {
                target.setHp(amt); humanRepository.save(target);
                saveBattleLog(healer, String.format("✨ Лекарь %s ВОСКРЕСИЛ павшего %s!", healer.getName(), target.getName()));
            }
        } else {
            if (isResurrectAction) return;
            target.setHp(target.getHp() + amt); humanRepository.save(target);
            saveBattleLog(healer, String.format("💚 Лекарь %s восполнил %s здоровье.", healer.getName(), target.getName()));
        }
    }

    private void executeHealerMagicAttack(Human healerObj, Human target) {
        if (target == null || target.getHp() == 0) return;
        Healer healer = (Healer) healerObj;
        int dmg = (healer.getDamage() / 2) + 2;

        if (target.getPosition() != null && target.getPosition() == 1) {
            dmg /= 2; target.setPosition(0);
        }

        target.setHp(Math.max(0, target.getHp() - dmg)); humanRepository.save(target);
        saveBattleLog(healer, String.format("🔮 Лекарь %s бьет магией по %s!", healer.getName(), target.getName()));
    }

    private void saveBattleLog(Human human, String message) {
        ActionLog log = new ActionLog();
        log.setActionText(message); log.setCreatedAt(LocalDateTime.now()); log.setHuman(human);
        actionLogRepository.save(log);
    }
}