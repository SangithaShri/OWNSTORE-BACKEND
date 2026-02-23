package com.example.Finzo.Repository;

import com.example.Finzo.Entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<ResultEntity, Long> {
    long countByWinner_Id(Long playerId);
    long countByLoser_Id(Long playerId);

    List<ResultEntity> findByWinner_IdOrLoser_Id(Long winnerId, Long loserId);
}
