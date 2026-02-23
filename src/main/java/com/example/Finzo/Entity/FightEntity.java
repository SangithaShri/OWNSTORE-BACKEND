package com.example.Finzo.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fights")

public class FightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fightId;

    @ManyToOne
    @JoinColumn(name = "player1_id")
    private PlayerEntity player1;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private PlayerEntity player2;

    private int betCoins;
    private int totalCoins;

    @Column(name = "player1_coins")
    private Integer player1Coins = 0;

    @Column(name = "player2_coins")
    private Integer player2Coins = 0;

//
//    @Column(name = "player1_paid_today")
//    private boolean player1PaidToday = false;
//
//    @Column(name = "player2_paid_today")
//    private boolean player2PaidToday = false;
//
//    @Column(name = "last_payment_date")
//    private String lastPaymentDate; // store date like "2026-02-13"


    @Enumerated(EnumType.STRING)
    private Status status = Status.ONGOING;

    @OneToMany(mappedBy = "fight")
    private List<PaymentEntity> payments;

    public int getPlayer1Coins() {
        return player1Coins;
    }

    public void setPlayer1Coins(int player1Coins) {
        this.player1Coins = player1Coins;
    }

    public int getPlayer2Coins() {
        return player2Coins;
    }

    public void setPlayer2Coins(int player2Coins) {
        this.player2Coins = player2Coins;
    }

    public enum Status {
        ONGOING, COMPLETED
    }
// Constructors!!
    public FightEntity() {
        this.player1Coins = 0;
        this.player2Coins = 0;
        this.totalCoins = 0;
    }

    public FightEntity(int fightId, int betCoins , int totalCoins, int player1Coins , int player2Coins) {
        this.fightId = fightId;
        this.betCoins = betCoins;
        this.totalCoins = totalCoins;
        this.player1Coins = player1Coins;
        this.player2Coins = player2Coins;
    }
    public FightEntity(PlayerEntity player1, PlayerEntity player2 , String lastPaymentDate , boolean player1PaidToday, boolean player2PaidToday) {
        this.player1 = player1;
        this.player2 = player2;
//        this.player1PaidToday = player1PaidToday;
//        this.player2PaidToday = player2PaidToday;
//        this.lastPaymentDate =  lastPaymentDate;
    }
    public FightEntity(Status status) {
        this.status = status;
    }
//Getters and Setters
    public int getFightId() {
        return fightId;
    }
    public void setFightId(int fightId) {
        this.fightId = fightId;
    }

    public PlayerEntity getPlayer1() {
        return player1;
    }
    public void setPlayer1(PlayerEntity player1) {
        this.player1 = player1;
    }

    public PlayerEntity getPlayer2() {
        return player2;
    }
    public void setPlayer2(PlayerEntity player2) {
        this.player2 = player2;
    }

    public int getBetCoins() {
        return betCoins;
    }
    public void setBetCoins(int betCoins) {
        this.betCoins = betCoins;
    }

    public int getTotalCoins() {
        return totalCoins;
    }
    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }


    @Column(name = "prev_player1_coins")
    private Integer prevPlayer1Coins = 0;

    @Column(name = "prev_player2_coins")
    private Integer prevPlayer2Coins = 0;

    // Add getters and setters
    public Integer getPrevPlayer1Coins() { return prevPlayer1Coins; }
    public void setPrevPlayer1Coins(Integer prevPlayer1Coins) { this.prevPlayer1Coins = prevPlayer1Coins; }

    public Integer getPrevPlayer2Coins() { return prevPlayer2Coins; }
    public void setPrevPlayer2Coins(Integer prevPlayer2Coins) { this.prevPlayer2Coins = prevPlayer2Coins; }


}
