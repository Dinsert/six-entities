package com.example.six_entities.repository;

import com.example.six_entities.model.OutboxEvent;
import com.example.six_entities.model.OutboxEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query(value = """
            SELECT *
            FROM app.outbox_events
            WHERE status = 'NEW'
            ORDER BY created_at
            FOR UPDATE SKIP LOCKED
            LIMIT 100
            """, nativeQuery = true)
    List<OutboxEvent> lockBatchForProcessing(OutboxEventStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE OutboxEvent e SET e.status=:status WHERE e.id = :id")
    void markStatus(@Param("id") UUID id, @Param("status") OutboxEventStatus status);
}
