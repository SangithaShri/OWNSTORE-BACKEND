package com.example.Finzo.Controllers;

import com.example.Finzo.Entity.FightEntity;
import com.example.Finzo.Entity.ResultEntity;
import com.example.Finzo.Repository.FightRepository;
import com.example.Finzo.Repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultRepository resultRepository;

    // ✅ ADD THIS — Needed to update fight status
    @Autowired
    private FightRepository fightRepository;


    // 🔹 GET — Fetch results by player
    @GetMapping("/player/{playerId}")
    public List<ResultEntity> getResultsByPlayer(@PathVariable Long playerId) {
        return resultRepository
                .findByWinner_IdOrLoser_Id(playerId, playerId);
    }


    // 🔹 POST — Create result + Complete fight
    @PostMapping
    public ResultEntity createResult(@RequestBody ResultEntity result){

        // 🪜 STEP 1 — Check fight is present in request
        if (result.getFight() == null) {
            throw new RuntimeException("Fight details are required to declare result");
        }

        // 🪜 STEP 2 — Get fightId from request
        int fightId = result.getFight().getFightId();

        // 🪜 STEP 3 — Fetch fight from DB
        FightEntity fight = fightRepository.findById(fightId)
                .orElseThrow(() -> new RuntimeException("Fight not found with id: " + fightId));

        // 🪜 STEP 4 — Update fight status → COMPLETED
        fight.setStatus(FightEntity.Status.COMPLETED);

        // 🪜 STEP 5 — Save updated fight
        fightRepository.save(fight);

        // 🪜 STEP 6 — Attach DB fight to result (important)
        result.setFight(fight);

        // 🪜 STEP 7 — Save result
        return resultRepository.save(result);
    }
    // Get total wins of a player
    @GetMapping("/wins/{playerId}")
    public long getTotalWins(@PathVariable Long playerId) {
        return resultRepository.countByWinner_Id(playerId);
    }

    // Get total losses of a player
    @GetMapping("/losses/{playerId}")
    public long getTotalLosses(@PathVariable Long playerId) {
        return resultRepository.countByLoser_Id(playerId);
    }
}