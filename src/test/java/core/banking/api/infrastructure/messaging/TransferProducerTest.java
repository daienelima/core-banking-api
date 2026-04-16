package core.banking.api.infrastructure.messaging;

import core.banking.api.infrastructure.config.KafkaTopicsProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferProducerTest {

    @Mock
    private KafkaTemplate<String, TransferEvent> kafkaTemplate;

    @Mock
    private KafkaTopicsProperties topics;

    @InjectMocks
    private TransferProducer producer;

    private TransferEvent transferEvent;
    private String topicName;

    @BeforeEach
    void setup() {
        transferEvent = new TransferEvent(1L, 2L, new BigDecimal("100.00"));
        topicName = "transfers";
    }

    @Test
    void shouldSendTransferEventToKafka() {
        when(topics.getTransfer()).thenReturn(topicName);
        when(kafkaTemplate.send(topicName, transferEvent))
                .thenReturn(CompletableFuture.completedFuture(new SendResult<>(null, null)));

        producer.send(transferEvent);

        verify(kafkaTemplate, times(1)).send(topicName, transferEvent);
    }


    @Test
    void shouldHandleTransferEventWithLargeAmount() {
        TransferEvent largeAmountEvent = new TransferEvent(1L, 2L, new BigDecimal("999999.99"));
        when(topics.getTransfer()).thenReturn(topicName);
        when(kafkaTemplate.send(topicName, largeAmountEvent))
                .thenReturn(CompletableFuture.completedFuture(new SendResult<>(null, null)));

        producer.send(largeAmountEvent);

        verify(kafkaTemplate, times(1)).send(topicName, largeAmountEvent);
    }

    @Test
    void shouldHandleTransferEventWithSmallAmount() {
        TransferEvent smallAmountEvent = new TransferEvent(1L, 2L, new BigDecimal("0.01"));
        when(topics.getTransfer()).thenReturn(topicName);
        when(kafkaTemplate.send(topicName, smallAmountEvent))
                .thenReturn(CompletableFuture.completedFuture(new SendResult<>(null, null)));

        producer.send(smallAmountEvent);

        verify(kafkaTemplate, times(1)).send(topicName, smallAmountEvent);
    }

    @Test
    void shouldHandleMultipleTransfersSentSequentially() {
        TransferEvent event1 = new TransferEvent(1L, 2L, new BigDecimal("100.00"));
        TransferEvent event2 = new TransferEvent(3L, 4L, new BigDecimal("50.00"));

        when(topics.getTransfer()).thenReturn(topicName);
        when(kafkaTemplate.send(eq(topicName), any(TransferEvent.class)))
                .thenReturn(CompletableFuture.completedFuture(new SendResult<>(null, null)));

        producer.send(event1);
        producer.send(event2);

        verify(kafkaTemplate, times(2)).send(eq(topicName), any(TransferEvent.class));
    }

    @Test
    void shouldSendEventWithSameAccountIds() {
        TransferEvent sameAccountsEvent = new TransferEvent(1L, 1L, new BigDecimal("100.00"));
        when(topics.getTransfer()).thenReturn(topicName);
        when(kafkaTemplate.send(topicName, sameAccountsEvent))
                .thenReturn(CompletableFuture.completedFuture(new SendResult<>(null, null)));

        producer.send(sameAccountsEvent);

        verify(kafkaTemplate, times(1)).send(topicName, sameAccountsEvent);
    }

    @Test
    void shouldSendEventWithNullTopic() {
        when(topics.getTransfer()).thenReturn(null);
        when(kafkaTemplate.send(null, transferEvent))
                .thenReturn(CompletableFuture.completedFuture(new SendResult<>(null, null)));

        producer.send(transferEvent);

        verify(kafkaTemplate, times(1)).send(null, transferEvent);
    }
}

