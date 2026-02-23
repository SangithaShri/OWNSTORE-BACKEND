package com.example.Finzo.Controllers;

import com.example.Finzo.Entity.FightEntity;
import com.example.Finzo.Entity.PaymentEntity;
import com.example.Finzo.Entity.PlayerEntity;
import com.example.Finzo.Entity.WithdrawalEntity;
import com.example.Finzo.Repository.FightRepository;
import com.example.Finzo.Repository.PlayerRepository;
import com.example.Finzo.Repository.PaymentRepository;
import com.example.Finzo.Repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@CrossOrigin
public class AdminPaymentController {

        @Autowired
        private PaymentRepository paymentRepository;

        @Autowired
        private PlayerRepository playerRepository;

        @Autowired
        private FightRepository fightRepository;

        @Autowired
        private WithdrawalRepository withdrawalRepository;

        @Autowired
        private JavaMailSender mailSender;

        // Accept Payment
        @PutMapping("/accept/{paymentId}")
        public ResponseEntity<?> acceptPayment(
                        @PathVariable Long paymentId) {

                // 1. Get payment
                PaymentEntity payment = paymentRepository.findById(paymentId)
                                .orElseThrow(() -> new RuntimeException("Payment not found"));

                // 2. Prevent double update
                if ("PAID".equals(payment.getStatus())) {
                        return ResponseEntity.badRequest()
                                        .body("Already accepted");
                }

                // 3. Mark as PAID
                payment.setStatus("PAID");
                paymentRepository.save(payment);

                // 4. Get fight & player
                FightEntity fight = payment.getFight();
                PlayerEntity player = payment.getPlayer();

                int amount = payment.getAmount();

                // 5. Update coins
                if (fight.getPlayer1().getId()
                                .equals(player.getId())) {

                        fight.setPlayer1Coins(
                                        fight.getPlayer1Coins() + amount);

                } else {

                        fight.setPlayer2Coins(
                                        fight.getPlayer2Coins() + amount);
                }

                // 6. Update pool
                fight.setTotalCoins(
                                fight.getTotalCoins() + amount);

                fightRepository.save(fight);

                return ResponseEntity.ok(
                                "Payment accepted & coins updated");
        }

        // Payments get Created
        @PostMapping("/create")
        public ResponseEntity<?> createPayment(
                        @RequestBody PaymentEntity paymentRequest) {

                if (paymentRequest.getFight() == null) {
                        throw new RuntimeException("Fight is missing in request");
                }

                if (paymentRequest.getPlayer() == null) {
                        throw new RuntimeException("Player is missing in request");
                }

                Integer fightId = paymentRequest.getFight().getFightId();
                Long playerId = paymentRequest.getPlayer().getId();

                // Fetch player
                PlayerEntity player = playerRepository
                                .findById(playerId)
                                .orElseThrow(() -> new RuntimeException("Player not found"));

                // Fetch fight
                FightEntity fight = fightRepository
                                .findById(fightId)
                                .orElseThrow(() -> new RuntimeException("Fight not found"));

                PaymentEntity payment = new PaymentEntity();
                payment.setAmount(paymentRequest.getAmount());
                payment.setPaymentDate(LocalDate.now());
                payment.setStatus("PENDING");
                payment.setPlayer(player);
                payment.setFight(fight);
                payment.setScreenshot(paymentRequest.getScreenshot());

                paymentRepository.save(payment);

                return ResponseEntity.ok("Payment submitted");
        }

        // get all payments
        @GetMapping("/all")
        public List<PaymentEntity> getAllPayments() {
                return paymentRepository.findAll();
        }

        // Reject Payment API
        @DeleteMapping("/reject/{paymentId}")
        public ResponseEntity<?> rejectPayment(@PathVariable Long paymentId) {

                // 1. Find the payment
                PaymentEntity payment = paymentRepository.findById(paymentId)
                                .orElseThrow(() -> new RuntimeException("Payment not found"));

                // 2. Get player info
                PlayerEntity player = payment.getPlayer();

                // 3. Send email
                try {
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setTo(player.getEmail()); // Make sure PlayerEntity has email field
                        message.setSubject("Payment Rejected");
                        message.setText("Your payment of ₹" + payment.getAmount() +
                                        " has been rejected. Please pay again to continue the fight.");
                        mailSender.send(message);
                } catch (Exception e) {
                        return ResponseEntity.status(500)
                                        .body("Failed to send email: " + e.getMessage());
                }

                // 4. Delete payment from table
                paymentRepository.delete(payment);

                return ResponseEntity.ok("Payment rejected and email sent to player");
        }

        // withdrawal
        @PutMapping("/withdraw/accept/{withdrawId}")
        public String acceptWithdrawRequest(@PathVariable Long withdrawId) {

                WithdrawalEntity withdrawal = withdrawalRepository.findById(withdrawId)
                                .orElseThrow(() -> new RuntimeException("Withdrawal request not found"));

                if (withdrawal.getStatus() != WithdrawalEntity.Status.PENDING) {
                        return "Already processed.";
                }

                withdrawal.setStatus(WithdrawalEntity.Status.APPROVED);
                withdrawalRepository.save(withdrawal);

                PlayerEntity player = withdrawal.getPlayer();

                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(player.getEmail());
                message.setSubject("Withdrawal Approved");
                message.setText(
                                "Dear " + player.getName() + ",\n\n" +
                                                "Rupees " + withdrawal.getAmount() +
                                                " is credited to your given UPI ID (" + withdrawal.getUpiId() + ").\n\n"
                                                +
                                                "Thank you for using Finzo.");

                mailSender.send(message);

                return "Withdrawal approved and mail sent.";
        }

        // Reject Withdrawal API
        @DeleteMapping("/withdraw/reject/{withdrawId}")
        public ResponseEntity<?> rejectWithdrawRequest(@PathVariable Long withdrawId) {
                WithdrawalEntity withdrawal = withdrawalRepository.findById(withdrawId)
                        .orElseThrow(() -> new RuntimeException("Withdrawal request not found"));

                if (withdrawal.getStatus() != WithdrawalEntity.Status.PENDING) {
                        return ResponseEntity.badRequest().body("Already processed.");
                }

                PlayerEntity player = withdrawal.getPlayer();

                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(player.getEmail());
                message.setSubject("Withdrawal Rejected");
                message.setText(
                        "Dear " + player.getName() + ",\n\n" +
                                " Your withdrawal request of Rupees " + withdrawal.getAmount() +
                                " has been rejected by the Admin. Your given UPI ID (" + withdrawal.getUpiId() + ").\n\n"
                                +
                                "Please resend your withdraw request again.");

                mailSender.send(message);

                // Delete withdrawal record (or mark as rejected if you prefer, but deletion
                // seems consistent with payment rejection)
                withdrawalRepository.delete(withdrawal);

                return ResponseEntity.ok("Withdrawal request rejected.");
        }

        // get payments by player
        @GetMapping("/player/{playerId}")
        public List<PaymentEntity> getPaymentsByPlayer(@PathVariable Long playerId) {
                return paymentRepository.findByPlayer_Id(playerId);
        }

}
