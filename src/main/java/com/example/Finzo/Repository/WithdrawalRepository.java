package com.example.Finzo.Repository;


import com.example.Finzo.Entity.WithdrawalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WithdrawalRepository extends JpaRepository<WithdrawalEntity, Long> {

    List<WithdrawalEntity> findByStatusIn(List<WithdrawalEntity.Status> statuses);
    List<WithdrawalEntity> findByPlayer_Id(Long playerId);
}
