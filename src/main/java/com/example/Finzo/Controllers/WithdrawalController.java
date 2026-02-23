package com.example.Finzo.Controllers;

import com.example.Finzo.Dto.WithdrawalResponseDTO;
import com.example.Finzo.Entity.FightEntity;
import com.example.Finzo.Entity.PlayerEntity;
import com.example.Finzo.Entity.WithdrawalEntity;
import com.example.Finzo.Repository.FightRepository;
import com.example.Finzo.Repository.PlayerRepository;
import com.example.Finzo.Repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdraw")
public class WithdrawalController {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private FightRepository fightRepository;

    //create withdrawal req
    @PostMapping("/request")
    public String requestWithdrawal(
            @RequestParam Long playerId,
            @RequestParam Long fightId,
            @RequestParam double amount,
            @RequestParam String upiId) {

        PlayerEntity player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        FightEntity fight = fightRepository.findById(fightId.intValue())
                .orElseThrow(() -> new RuntimeException("Fight not found"));

        WithdrawalEntity withdrawal = new WithdrawalEntity();
        withdrawal.setPlayer(player);
        withdrawal.setFight(fight);   // 🔥 VERY IMPORTANT
        withdrawal.setAmount(amount);
        withdrawal.setUpiId(upiId);
        withdrawal.setStatus(WithdrawalEntity.Status.PENDING);

        withdrawalRepository.save(withdrawal);

        return "Withdrawal request submitted successfully.";
    }
    //check pending withdrawal requests
    @GetMapping("/admin")
    public List<WithdrawalResponseDTO> getAdminWithdrawals() {
        return withdrawalRepository
                .findByStatusIn(List.of(
                        WithdrawalEntity.Status.PENDING,
                        WithdrawalEntity.Status.APPROVED
                ))
                .stream()
                .map(WithdrawalResponseDTO::new)
                .toList();
    }

    @GetMapping("/player/{playerId}")
    public List<WithdrawalEntity> getWithdrawalsByPlayer(
            @PathVariable Long playerId) {

        return withdrawalRepository.findByPlayer_Id(playerId);
    }
}
