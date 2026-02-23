package com.example.Finzo.Repository;

import com.example.Finzo.Entity.InviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InviteRepository extends JpaRepository<InviteEntity, Long> {
    List<InviteEntity> findBySender_Id(int id);
    List<InviteEntity> findByReceiver_Id(int id);

//    List<InviteEntity> findByReceiver_IdAndStatus(int receiverId, InviteEntity.Status status);

    List<InviteEntity> findByReceiver_IdAndStatus(Long receiverId, InviteEntity.Status status);

}
