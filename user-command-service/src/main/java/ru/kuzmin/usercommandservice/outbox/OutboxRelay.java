package ru.kuzmin.usercommandservice.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Relay transactional outbox: периодически публикует неотправленные события в Kafka
 * с ключом по aggregateId. Гарантия at-least-once: отметка sent только после успешной отправки.
 */
@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;
    private final int batchSize;

    public OutboxRelay(OutboxEventRepository outboxEventRepository,
                       KafkaTemplate<String, String> kafkaTemplate,
                       @Value("${app.kafka.topic}") String topic,
                       @Value("${app.outbox.batch-size:100}") int batchSize) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.batchSize = batchSize;
    }

    @Scheduled(fixedDelayString = "${app.outbox.relay-delay-ms:1000}")
    public void relay() {
        for (OutboxEvent event : outboxEventRepository.findBySentFalseOrderByIdAsc(PageRequest.of(0, batchSize))) {
            try {
                kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload())
                        .get(5, TimeUnit.SECONDS);
                event.markSent();
                outboxEventRepository.save(event);
            } catch (Exception e) {
                log.error("Failed to publish outbox event id={}, eventType={}", event.getId(), event.getEventType(), e);
            }
        }
    }
}
