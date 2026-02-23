package com.example.Finzo.Controllers;

import com.example.Finzo.Entity.FightEntity;
import com.example.Finzo.Entity.PlayerEntity;
import com.example.Finzo.Entity.ResultEntity;
import com.example.Finzo.Repository.FightRepository;
import com.example.Finzo.Repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/fight-results")
public class FightResultScheduler {

    @Autowired
    private FightRepository fightRepository;

    @Autowired
    private ResultRepository resultRepository;

    // Scheduled at 11:59 PM daily
    @Scheduled(cron = "0 59 23 * * ?")
    @PostMapping("/check-winner")
    public ResponseEntity<?> checkDailyFightWinners() {
        try {
            List<FightEntity> ongoingFights = fightRepository.findByStatus(FightEntity.Status.ONGOING);
            int winnersDeclared = 0;

            for (FightEntity fight : ongoingFights) {
                int p1Coins = fight.getPlayer1Coins();
                int p2Coins = fight.getPlayer2Coins();
                int prevP1 = fight.getPrevPlayer1Coins();
                int prevP2 = fight.getPrevPlayer2Coins();

                boolean p1Increased = p1Coins > prevP1;
                boolean p2Increased = p2Coins > prevP2;
                // one paid and another did not pay
                if (p1Increased ^ p2Increased) {
                    PlayerEntity winnerPlayer = p1Increased ? fight.getPlayer1() : fight.getPlayer2();
                    PlayerEntity loserPlayer = p1Increased ? fight.getPlayer2() : fight.getPlayer1();

                    ResultEntity result = new ResultEntity();
                    result.setFight(fight);
                    result.setPlayer1(fight.getPlayer1());
                    result.setPlayer2(fight.getPlayer2());
                    result.setWinner(winnerPlayer);
                    result.setLoser(loserPlayer);

                    resultRepository.save(result);

                    fight.setStatus(FightEntity.Status.COMPLETED);
                    winnersDeclared++;
                }
                // 🔴 BOTH DID NOT PAY
                if (!p1Increased && !p2Increased) {

                    ResultEntity result = new ResultEntity();
                    result.setFight(fight);
                    result.setPlayer1(fight.getPlayer1());
                    result.setPlayer2(fight.getPlayer2());

                    // No winner
                    result.setWinner(null);
                    result.setLoser(null);

                    resultRepository.save(result);

                    fight.setStatus(FightEntity.Status.COMPLETED);

                    winnersDeclared++;
                }

                fight.setPrevPlayer1Coins(p1Coins);
                fight.setPrevPlayer2Coins(p2Coins);
                fightRepository.save(fight);
            }

            return ResponseEntity.ok(Map.of("message", winnersDeclared + " fights updated with winners today."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}