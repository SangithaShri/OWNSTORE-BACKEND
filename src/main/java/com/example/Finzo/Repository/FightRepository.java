package com.example.Finzo.Repository;

import com.example.Finzo.Entity.FightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FightRepository extends JpaRepository<FightEntity, Integer> {

    @Query("""
    SELECT 
        f.fightId,
        CASE 
            WHEN p1.name = :name THEN p2.name
            ELSE p1.name
        END,
        f.betCoins,
        f.totalCoins,
        f.status
    FROM FightEntity f
    JOIN f.player1 p1
    JOIN f.player2 p2
    WHERE p1.name = :name OR p2.name = :name
    """)
    List<Object[]> findFightsByPlayerName(@Param("name") String name);

    List<FightEntity> findByStatus(FightEntity.Status status);

    @Query("""
    SELECT f FROM FightEntity f
    WHERE f.status = 'ONGOING'
    AND (f.player1.id = :playerId
         OR f.player2.id = :playerId)
""")
    List<FightEntity> findOngoingFightsByPlayerId(@Param("playerId") Long playerId);

//    List<FightEntity> findByStatusAndPlayer1_IdOrStatusAndPlayer2_Id(
//            FightEntity.Status status1, Long player1Id,
//            FightEntity.Status status2, Long player2Id);
}

