package com.example.six_entities.scheduler;

import com.example.six_entities.model.OutboxEvent;
import com.example.six_entities.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @Scheduled(fixedDelay = 3000)
    public void publish() {
        List<OutboxEvent> events =
                outboxEventRepository.findTop100ByStatusOrderByCreatedAt("NEW");

        for (OutboxEvent event : events) {
            kafkaTemplate.send(
                    "user-profile-events",
                    event.getAggregateId().toString(),
                    event.getPayload()
            );

            event.setStatus("SENT");
            event.setProcessedAt(Instant.now());
        }
    }
}