package com.example.Finzo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "result")
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resultId;

    @ManyToOne
    @JoinColumn(name = "player1_id")
    private PlayerEntity player1;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private PlayerEntity player2;

    // ✅ ADD THIS — LINK TO FIGHT
    @ManyToOne
    @JoinColumn(name = "fight_id")
    private FightEntity fight;

    // ✅ Add winner & loser
    @ManyToOne
    @JoinColumn(name = "winner_id")
    private PlayerEntity winner;

    @ManyToOne
    @JoinColumn(name = "loser_id")
    private PlayerEntity loser;

    // Constructors
    public ResultEntity() {}

    public ResultEntity(PlayerEntity player1, PlayerEntity player2, FightEntity fight,PlayerEntity winner, PlayerEntity loser) {
        this.player1 = player1;
        this.player2 = player2;
        this.fight = fight;
        this.winner = winner;
        this.loser = loser;
    }

    // Getters & Setters

    public int getResultId() {
        return resultId;
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
    public FightEntity getFight() {
        return fight;
    }
    public void setFight(FightEntity fight) {
        this.fight = fight;
    }
    public PlayerEntity getWinner() { return winner; }
    public void setWinner(PlayerEntity winner) { this.winner = winner; }
    public PlayerEntity getLoser() { return loser; }
    public void setLoser(PlayerEntity loser) { this.loser = loser; }
}