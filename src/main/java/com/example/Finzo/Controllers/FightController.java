package com.example.Finzo.Controllers;

import com.example.Finzo.Entity.FightEntity;
import com.example.Finzo.Repository.FightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fights")
public class FightController {

        @Autowired
        private FightRepository fightRepository;

        @GetMapping
        public List<FightEntity> getfights() {
            return fightRepository.findAll();
        }

        @PostMapping
        public FightEntity createFights(@RequestBody FightEntity fights){
            return fightRepository.save(fights);
        }

        // for getting fight details for particular person
        @GetMapping("/byPlayer")
        public List<Map<String, Object>> getFightsByPlayer(@RequestParam String name) {

            List<Object[]> results = fightRepository.findFightsByPlayerName(name);

            return results.stream().map(obj -> {
                Map<String, Object> map = new HashMap<>();
                map.put("fightId", obj[0]);
                map.put("opponentName", obj[1]);
                map.put("betCoins", obj[2]);
                map.put("totalCoins", obj[3]);
                map.put("status", obj[4]);
                return map;
            }).toList();
        }

        // ✅ ONGOING FIGHTS
        @GetMapping("/ongoing/{playerId}")
        public List<FightEntity> getOngoingFights(@PathVariable Long playerId) {
            return fightRepository.findOngoingFightsByPlayerId(playerId);
        }

        // ✅ COMPLETED FIGHTS
        @GetMapping("/completed")
        public List<FightEntity> getCompletedFights() {
            return fightRepository.findByStatus(FightEntity.Status.COMPLETED);
        }

}
