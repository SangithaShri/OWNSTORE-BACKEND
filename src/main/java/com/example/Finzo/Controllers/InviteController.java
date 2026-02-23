package com.example.Finzo.Controllers;

import com.example.Finzo.Dto.InviteDTO;
import com.example.Finzo.Entity.FightEntity;
import com.example.Finzo.Entity.InviteEntity;
import com.example.Finzo.Entity.PlayerEntity;
import com.example.Finzo.Repository.FightRepository;
import com.example.Finzo.Repository.InviteRepository;
import com.example.Finzo.Repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invites")
public class InviteController {

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private FightRepository fightRepository;

    // ✅ Get all invites
    @GetMapping
    public List<InviteEntity> getInvites() {
        return inviteRepository.findAll();
    }

    //receive invites
    @GetMapping("/received/{playerId}")
    public List<InviteEntity> getReceivedInvites(@PathVariable Long playerId) {
        return inviteRepository
                .findByReceiver_IdAndStatus(playerId, InviteEntity.Status.SENT);
    }

    // ✅ Create invite using DTO
    @PostMapping
    public InviteEntity createInvite(@RequestBody InviteDTO request) {

        PlayerEntity sender = playerRepository
                .findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        PlayerEntity receiver = playerRepository
                .findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        InviteEntity invite = new InviteEntity();
        invite.setSender(sender);
        invite.setReceiver(receiver);
        invite.setAmount((int) request.getAmount());
        invite.setStatus(InviteEntity.Status.SENT);

        return inviteRepository.save(invite);
    }

    // ✅ Accept / Reject Invite
    @PutMapping("/{inviteId}")
    public String updateInviteStatus(@PathVariable int inviteId,
                                     @RequestParam String action) {

        InviteEntity invite = inviteRepository.findById((long) inviteId)
                .orElseThrow(() -> new RuntimeException("Invite not found"));

        if (action.equalsIgnoreCase("ACCEPT")) {

            // 🔥 Create Fight
            FightEntity fight = new FightEntity();
            fight.setPlayer1(invite.getSender());
            fight.setPlayer2(invite.getReceiver());
            fight.setBetCoins(invite.getAmount());
            fight.setTotalCoins(0);
            fight.setPlayer1Coins(0);
            fight.setPlayer2Coins(0);

            fight.setStatus(FightEntity.Status.ONGOING);

            fightRepository.save(fight);

            invite.setStatus(InviteEntity.Status.ACCEPTED);
            inviteRepository.save(invite);

            // Delete invite
//            inviteRepository.delete(invite);

            return "Invite accepted and fight created!";
        }

        else if (action.equalsIgnoreCase("REJECT")) {

            inviteRepository.delete(invite);
            return "Invite rejected and deleted!";
        }

        return "Invalid action! Use ACCEPT or REJECT";
    }
}