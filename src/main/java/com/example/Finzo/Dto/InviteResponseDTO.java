package com.example.Finzo.Dto;

import java.util.List;

public class InviteResponseDTO {

    private List<com.example.Finzo.Dto.InviteDTO> sentInvites;
    private List<com.example.Finzo.Dto.InviteDTO> receivedInvites;

    public InviteResponseDTO(List<com.example.Finzo.Dto.InviteDTO> sentInvites,
                             List<com.example.Finzo.Dto.InviteDTO> receivedInvites) {
        this.sentInvites = sentInvites;
        this.receivedInvites = receivedInvites;
    }

    public List<com.example.Finzo.Dto.InviteDTO> getSentInvites() { return sentInvites; }
    public List<com.example.Finzo.Dto.InviteDTO> getReceivedInvites() { return receivedInvites; }
}
