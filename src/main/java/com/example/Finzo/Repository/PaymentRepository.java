package com.example.Finzo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Finzo.Entity.PaymentEntity;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByPlayer_Id(Long playerId);
}
