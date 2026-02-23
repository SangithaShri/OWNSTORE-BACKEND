package com.example.Finzo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="players")

public class PlayerEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    private Long id;
    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private String phonenumber;
    private String otp;
    private Long otpExpiryTime;

    //Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Long getOtpExpiryTime() {
        return otpExpiryTime;
    }

    public void setOtpExpiryTime(Long otpExpiryTime) {
        this.otpExpiryTime = otpExpiryTime;
    }


    //Constructors
    public PlayerEntity() {
    }
    public PlayerEntity(Long id) {
        this.id = id;
    }
    public PlayerEntity(String name, String email, String password, String phonenumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
    }
    public PlayerEntity(String otp, Long otpExpiryTime) {
        this.otp = otp;
        this.otpExpiryTime = otpExpiryTime;
    }


}
