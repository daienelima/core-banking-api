package core.banking.api.integration;

import core.banking.api.infrastructure.messaging.TransferEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

import java.math.BigDecimal;
import java.util.Map;

@SpringBootTest
@Testcontainers
class TransferIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("core_banking_db")
                    .withUsername("postgres")
                    .withPassword("password");

    @Container
    static KafkaContainer kafka =
            new KafkaContainer("confluentinc/cp-kafka:7.5.0");

    @Autowired
    private KafkaTemplate<String, TransferEvent> kafkaTemplate;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    void shouldSendEventToKafkaSuccessfully() throws Exception {

        try (AdminClient admin = AdminClient.create(
                Map.of("bootstrap.servers", kafka.getBootstrapServers()))) {

            admin.createTopics(
                    java.util.List.of(new NewTopic("transfer-topic", 1, (short) 1))
            ).all().get();
        }

        TransferEvent event = new TransferEvent(
                1L,
                2L,
                BigDecimal.valueOf(100)
        );

        kafkaTemplate.send("transfer-topic", event);
    }
}