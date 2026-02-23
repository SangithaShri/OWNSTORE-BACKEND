package com.example.Finzo.Dto;

import com.example.Finzo.Entity.WithdrawalEntity;
public class WithdrawalResponseDTO {

    private Long id;
    private String requestTime;
    private String upiId;
    private double amount;
    private String status;
    private Object player;
    private Object fight;

    public WithdrawalResponseDTO(WithdrawalEntity withdrawal) {
        this.id = withdrawal.getId();
        this.requestTime = withdrawal.getRequestTime().toString();
        this.upiId = withdrawal.getUpiId();
        this.amount = withdrawal.getAmount();
        this.status = withdrawal.getStatus().name();
        this.player = withdrawal.getPlayer();
        this.fight = withdrawal.getFight();
    }

    public Long getId() { return id; }
    public String getRequestTime() { return requestTime; }
    public String getUpiId() { return upiId; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public Object getPlayer() { return player; }
    public Object getFight() { return fight; }
}
