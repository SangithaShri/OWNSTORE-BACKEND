package com.example.Finzo.Dto;

public class PlayerDTO {

    private int id;
    private String name;
    private String email;
    private String phonenumber;

    public PlayerDTO(int id, String name, String email, String phonenumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhonenumber() { return phonenumber; }
}
