package com.example.six_entities.scheduler;

import com.example.six_entities.model.OutboxEvent;
import com.example.six_entities.model.OutboxEventStatus;
import com.example.six_entities.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    @Value("${topic.name}")
    private String topic;

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @Scheduled(fixedDelay = 3000)
    public void publish() {
        List<OutboxEvent> events = outboxEventRepository.lockBatchForProcessing(OutboxEventStatus.NEW);
        for (OutboxEvent event : events) {
            kafkaTemplate.send(topic, event.getMessageKey(), event.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send event {} to Kafka", event.getId(), ex);
                        } else {
                            log.info("Successfully sent event {} to Kafka, offset={}", event.getId(), result.getRecordMetadata().offset());
                            outboxEventRepository.markStatus(event.getId(), OutboxEventStatus.SENT);
                        }
                    });
            log.info("Send to Kafka in transaction key={}, payload={}", event.getMessageKey(), event.getPayload());
        }
    }
}