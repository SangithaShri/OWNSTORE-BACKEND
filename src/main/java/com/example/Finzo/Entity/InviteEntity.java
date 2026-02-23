package com.example.Finzo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "invites")

public class InviteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inviteId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private PlayerEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private PlayerEntity receiver;

    private int amount;

    @Enumerated(EnumType.STRING)
    private Status status = Status.SENT;
    public enum Status {
        SENT, ACCEPTED, REJECTED
    }

//Constructors
    public InviteEntity() {
    }
    public InviteEntity(int inviteId, int amount) {
        this.inviteId = inviteId;
        this.amount = amount;
    }
    public InviteEntity(PlayerEntity sender, PlayerEntity receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
    public InviteEntity(Status status) {
        this.status = status;
    }

//Getters and Setters
    public int getInviteId() {
        return inviteId;
    }
    public void setInviteId(int inviteId) {
        this.inviteId = inviteId;
    }

    public PlayerEntity getSender() {
        return sender;
    }
    public void setSender(PlayerEntity sender) {
        this.sender = sender;
    }

    public PlayerEntity getReceiver() {
        return receiver;
    }
    public void setReceiver(PlayerEntity receiver) {
        this.receiver = receiver;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
