package com.example.six_entities.scheduler;

import com.example.six_entities.model.OutboxEvent;
import com.example.six_entities.model.OutboxEventStatus;
import com.example.six_entities.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    public static final String USER_PROFILE_EVENTS = "user-profile-events";

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @Scheduled(fixedDelay = 3000)
    public void publish() {
        List<OutboxEvent> events = outboxEventRepository.findTop100ByStatusOrderByCreatedAt(OutboxEventStatus.NEW);
        kafkaTemplate.executeInTransaction(kt -> {
            for (OutboxEvent event : events) {
                kafkaTemplate.send(USER_PROFILE_EVENTS, event.getPayload());
                log.info("Send to Kafka in transaction event payload: {}", event.getPayload());
                event.setStatus(OutboxEventStatus.SENT);
            }
            return true;
        });
    }
}