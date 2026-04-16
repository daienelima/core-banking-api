package core.banking.api.infrastructure.messaging;

import core.banking.api.domain.service.NotificationService;
import core.banking.api.domain.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaNotificationService implements NotificationService {

    private final TransferProducer producer;

    public KafkaNotificationService(TransferProducer producer) {
        this.producer = producer;
    }

    @Override
    public void notifyTransfer(Account from, Account to, BigDecimal amount) {
        TransferEvent event = new TransferEvent(from.id(), to.id(), amount);
        producer.send(event);
    }
}