package core.banking.api.integration;

import core.banking.api.infrastructure.messaging.TransferEvent;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransferIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private KafkaTemplate<String, TransferEvent> kafkaTemplate;

    @Test
    void shouldSendEventToKafkaSuccessfully() throws Exception {

        try (AdminClient admin = AdminClient.create(
                Map.of("bootstrap.servers", kafka.getBootstrapServers()))) {

            var topics = admin.listTopics().names().get();
            if (!topics.contains("transfer-topic")) {
                admin.createTopics(
                        java.util.List.of(new NewTopic("transfer-topic", 1, (short) 1))
                ).all().get();
            }
        }

        TransferEvent event = new TransferEvent(
                1L,
                2L,
                BigDecimal.valueOf(100)
        );

        var sendResult = kafkaTemplate.send("transfer-topic", event);

        assertNotNull(sendResult);
        assertNotNull(sendResult.get());
        assertNotNull(sendResult.get().getRecordMetadata());

        assertEquals(1L, event.fromAccountId());
        assertEquals(2L, event.toAccountId());
        assertEquals(0, event.amount().compareTo(BigDecimal.valueOf(100)));
    }
}