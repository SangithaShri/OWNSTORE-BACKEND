package com.example.Finzo.Repository;

import com.example.Finzo.Entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository <PlayerEntity, Long> {
    @Query("SELECT p FROM PlayerEntity p WHERE p.email = :email AND p.password = :password")
    Optional<PlayerEntity> login(
            @Param("email") String email,
            @Param("password") String password
    );
    Optional<PlayerEntity> findByEmail(String email);



}
