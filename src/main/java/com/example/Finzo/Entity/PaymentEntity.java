package com.example.Finzo.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Amount paid daily
    private int amount;

    // Payment date
    private LocalDate paymentDate;

    // Status → PENDING / PAID
    private String status;

    // Player who paid
    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerEntity player;

    // Fight reference
    @ManyToOne
    @JoinColumn(name = "fight_id")
    private FightEntity fight;

    // Screenshot Proof (Base64)
    @Column(columnDefinition = "LONGTEXT")
    private String screenshot;

    // Getters & Setters

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    public FightEntity getFight() {
        return fight;
    }

    public void setFight(FightEntity fight) {
        this.fight = fight;
    }
}
