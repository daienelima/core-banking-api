package core.banking.api.infrastructure.messaging;

import core.banking.api.domain.exception.ErroEnvioNotificacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    private NotificationConsumer consumer;
    private TransferEvent validEvent;

    @BeforeEach
    void setup() {
        consumer = new NotificationConsumer();
        validEvent = new TransferEvent(1L, 2L, new BigDecimal("100.00"));
    }

    @Test
    void shouldProcessValidEvent() {
        assertDoesNotThrow(() -> consumer.consume(validEvent));
    }

    @Test
    void shouldProcessEventWithoutException() {
        assertDoesNotThrow(() -> consumer.process(validEvent));
    }

    @Test
    void shouldThrowExceptionWhenProcessingNullEvent() {
        assertThrows(ErroEnvioNotificacaoException.class, () -> consumer.process(null));
    }

    @Test
    void shouldNotThrowExceptionWhenConsumeFailsBecauseItHasTryCatch() {
        assertDoesNotThrow(() -> consumer.consume(null));
    }

    @Test
    void shouldProcessMultipleEventsSequentially() {
        TransferEvent event1 = new TransferEvent(1L, 2L, new BigDecimal("100.00"));
        TransferEvent event2 = new TransferEvent(3L, 4L, new BigDecimal("200.00"));

        assertDoesNotThrow(() -> {
            consumer.consume(event1);
            consumer.consume(event2);
        });
    }

    @Test
    void shouldHandleEventWithLargeAmount() {
        TransferEvent largeEvent = new TransferEvent(1L, 2L, new BigDecimal("999999.99"));

        assertDoesNotThrow(() -> consumer.consume(largeEvent));
    }

    @Test
    void shouldHandleEventWithSmallAmount() {
        TransferEvent smallEvent = new TransferEvent(1L, 2L, new BigDecimal("0.01"));

        assertDoesNotThrow(() -> consumer.consume(smallEvent));
    }

    @Test
    void shouldHandleEventWithZeroAmount() {
        TransferEvent zeroEvent = new TransferEvent(1L, 2L, BigDecimal.ZERO);

        assertDoesNotThrow(() -> consumer.consume(zeroEvent));
    }

    @Test
    void shouldHandleEventWithSameAccountIds() {
        TransferEvent sameAccountEvent = new TransferEvent(1L, 1L, new BigDecimal("100.00"));

        assertDoesNotThrow(() -> consumer.consume(sameAccountEvent));
    }

}

