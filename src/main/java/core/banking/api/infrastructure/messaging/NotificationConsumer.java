package core.banking.api.infrastructure.messaging;

import core.banking.api.domain.exception.ErroEnvioNotificacaoException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @KafkaListener(topics = "${app.kafka.topics.transfer}", groupId = "${app.kafka.consumer.group-id}")
    public void consume(TransferEvent event) {
        try {
            logger.info("Evento recebido: [{}]", event);
            process(event);
        } catch (Exception ex) {
            fallback(event, ex);
        }
    }

    @Retry(name = "notificationService")
    @CircuitBreaker(name = "notificationService")
    public void process(TransferEvent event) {
        enviarNotificacao(event);

        logger.info("Notificação enviada com sucesso: [{}]", event);
    }

    private void enviarNotificacao(TransferEvent event) {
        if (Objects.isNull(event)) {
            throw new ErroEnvioNotificacaoException();
        }
    }

    public void fallback(TransferEvent event, Throwable ex) {
        logger.warn("Erro ao processar evento [{}]: {}", event, ex.getMessage());
    }
}