package com.example.six_entities.scheduler;

import com.example.six_entities.model.OutboxEvent;
import com.example.six_entities.model.OutboxEventStatus;
import com.example.six_entities.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
                outboxEventRepository.findTop100ByStatusOrderByCreatedAt(OutboxEventStatus.NEW);

        for (OutboxEvent event : events) {
            kafkaTemplate.send(
                    "user-profile-events",
                    event.getPayload()
            );

            event.setStatus(OutboxEventStatus.SENT);
        }
    }
}