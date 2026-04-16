package core.banking.api.infrastructure.messaging;

import core.banking.api.infrastructure.config.KafkaTopicsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransferProducer {

    private static final Logger logger = LoggerFactory.getLogger(TransferProducer.class);

    private final KafkaTemplate<String, TransferEvent> kafkaTemplate;
    private final KafkaTopicsProperties topics;

    public TransferProducer(KafkaTemplate<String, TransferEvent> kafkaTemplate, KafkaTopicsProperties topics) {
        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
    }

    public void send(TransferEvent event) {
        kafkaTemplate.send(topics.getTransfer(), event);
        logger.info("Transferencia enviada [{}] para o topico [{}]", event, topics.getTransfer());
    }
}