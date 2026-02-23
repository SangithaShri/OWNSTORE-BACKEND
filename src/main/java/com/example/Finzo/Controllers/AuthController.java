package com.example.Finzo.Controllers;

import com.example.Finzo.Entity.PlayerEntity;
import com.example.Finzo.Repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;


@RestController
@RequestMapping("/api/players")
public class AuthController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping
    public List<PlayerEntity> getplayers() {
        return playerRepository.findAll();
    }

    @PostMapping
    public PlayerEntity createPlayer(@RequestBody PlayerEntity player){
        return playerRepository.save(player);
    }

    //login
    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> loginPlayer(
            @RequestParam String email,
            @RequestParam String password) {

        Optional<PlayerEntity> optionalPlayer =
                playerRepository.login(email.trim(), password.trim());

        Map<String, Object> response = new HashMap<>();

        if (optionalPlayer.isPresent()) {

            PlayerEntity player = optionalPlayer.get();

            response.put("success", true);
            response.put("id", player.getId());
            response.put("name", player.getName());

            return ResponseEntity.ok(response);

        } else {

            response.put("success", false);
            response.put("message", "Invalid email or password");

            return ResponseEntity.badRequest().body(response);
        }
    }

    //for getting all player names
    @GetMapping("/names")
    public List<String> getAllPlayerNames() {
        return playerRepository.findAll()
                .stream()
                .map(PlayerEntity::getName)
                .toList();
    }

    @Autowired
    private JavaMailSender mailSender;

    // SEND OTP
    @PostMapping("/forgot-password")
    public String sendOtp(@RequestParam String email) {

        Optional<PlayerEntity> optionalPlayer = playerRepository.findByEmail(email);

        if (optionalPlayer.isEmpty()) {
            return "Email not found!";
        }

        PlayerEntity player = optionalPlayer.get();

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        player.setOtp(otp);
        player.setOtpExpiryTime(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 mins expiry

        playerRepository.save(player);

        // Send Email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Finzo Password Reset OTP");
            message.setText("Your OTP is: " + otp + "\nValid for 5 minutes.");
            mailSender.send(message);
            System.out.println("OTP sent: " + otp + " to " + email);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send OTP email: " + e.getMessage();
        }

        return "OTP sent to your email!";
    }

//RESET PASSWORD
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {

        Optional<PlayerEntity> optionalPlayer = playerRepository.findByEmail(email);

        if (optionalPlayer.isEmpty()) {
            return "Invalid email!";
        }

        PlayerEntity player = optionalPlayer.get();

        // Check OTP match
        if (player.getOtp() == null || !player.getOtp().equals(otp)) {
            return "Invalid OTP!";
        }

        // Check expiry
        if (player.getOtpExpiryTime() == null ||
                System.currentTimeMillis() > player.getOtpExpiryTime()) {
            return "OTP expired!";
        }

        // Update password
        player.setPassword(newPassword);

        // Clear OTP after successful reset
        player.setOtp(null);
        player.setOtpExpiryTime(null);

        playerRepository.save(player);

        return "Password reset successful!";
    }

    //Verify OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        Optional<PlayerEntity> optionalPlayer = playerRepository.findByEmail(email);

        if (optionalPlayer.isEmpty()) {
            return "Invalid email!";
        }

        PlayerEntity player = optionalPlayer.get();

        if (player.getOtp() == null || !player.getOtp().equals(otp)) {
            return "Invalid OTP!";
        }

        if (player.getOtpExpiryTime() == null ||
                System.currentTimeMillis() > player.getOtpExpiryTime()) {
            return "OTP expired!";
        }

        return "OTP verified successfully!";
    }


}