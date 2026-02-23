package com.example.Finzo.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.Finzo.Entity.FightEntity;

@Entity
@Table(name = "withdrawals")
public class WithdrawalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerEntity player;

    @ManyToOne
    @JoinColumn(name = "fight_id")
    private FightEntity fight;

    private double amount;

    private String upiId;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime requestTime = LocalDateTime.now();

    public enum Status {
        PENDING,
        APPROVED
    }

    // Getters and Setters

    public Long getId() { return id; }

    public PlayerEntity getPlayer() { return player; }
    public void setPlayer(PlayerEntity player) { this.player = player; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getRequestTime() { return requestTime; }

    public FightEntity getFight() {
        return fight;
    }
    public void setFight(FightEntity fight) {
        this.fight = fight;
    }
}
