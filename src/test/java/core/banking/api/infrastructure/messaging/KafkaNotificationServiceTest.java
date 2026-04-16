package core.banking.api.infrastructure.messaging;

import core.banking.api.domain.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaNotificationServiceTest {

    @Mock
    private TransferProducer producer;

    @InjectMocks
    private KafkaNotificationService service;

    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;

    @BeforeEach
    void setup() {
        fromAccount = new Account(1L, "John", new BigDecimal("1000.00"));
        toAccount = new Account(2L, "Jane", new BigDecimal("500.00"));
        amount = new BigDecimal("100.00");
    }

    @Test
    void shouldNotifyTransferWithValidAccounts() {
        service.notifyTransfer(fromAccount, toAccount, amount);

        ArgumentCaptor<TransferEvent> eventCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        verify(producer, times(1)).send(eventCaptor.capture());

        TransferEvent event = eventCaptor.getValue();
        assertEquals(1L, event.fromAccountId());
        assertEquals(2L, event.toAccountId());
        assertEquals(amount, event.amount());
    }

    @Test
    void shouldCreateTransferEventWithCorrectAccountIds() {
        service.notifyTransfer(fromAccount, toAccount, amount);

        ArgumentCaptor<TransferEvent> eventCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        verify(producer, times(1)).send(eventCaptor.capture());

        TransferEvent event = eventCaptor.getValue();
        assertEquals(fromAccount.id(), event.fromAccountId());
        assertEquals(toAccount.id(), event.toAccountId());
    }

    @Test
    void shouldCreateTransferEventWithCorrectAmount() {
        service.notifyTransfer(fromAccount, toAccount, amount);

        ArgumentCaptor<TransferEvent> eventCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        verify(producer, times(1)).send(eventCaptor.capture());

        TransferEvent event = eventCaptor.getValue();
        assertEquals(amount, event.amount());
    }

    @Test
    void shouldNotifyTransferWithLargeAmount() {
        BigDecimal largeAmount = new BigDecimal("999999.99");

        service.notifyTransfer(fromAccount, toAccount, largeAmount);

        ArgumentCaptor<TransferEvent> eventCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        verify(producer, times(1)).send(eventCaptor.capture());

        TransferEvent event = eventCaptor.getValue();
        assertEquals(largeAmount, event.amount());
    }

    @Test
    void shouldNotifyTransferWithSmallAmount() {
        BigDecimal smallAmount = new BigDecimal("0.01");

        service.notifyTransfer(fromAccount, toAccount, smallAmount);

        ArgumentCaptor<TransferEvent> eventCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        verify(producer, times(1)).send(eventCaptor.capture());

        TransferEvent event = eventCaptor.getValue();
        assertEquals(smallAmount, event.amount());
    }

    @Test
    void shouldNotifyTransferBetweenSameAccount() {
        Account sameAccount = new Account(1L, "John", new BigDecimal("1000.00"));

        service.notifyTransfer(fromAccount, sameAccount, amount);

        ArgumentCaptor<TransferEvent> eventCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        verify(producer, times(1)).send(eventCaptor.capture());

        TransferEvent event = eventCaptor.getValue();
        assertEquals(fromAccount.id(), event.fromAccountId());
        assertEquals(sameAccount.id(), event.toAccountId());
    }

    @Test
    void shouldNotifyTransferWithDifferentAccountNames() {
        Account account1 = new Account(1L, "Alice", new BigDecimal("1000.00"));
        Account account2 = new Account(2L, "Bob", new BigDecimal("500.00"));

        service.notifyTransfer(account1, account2, amount);

        ArgumentCaptor<TransferEvent> eventCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        verify(producer, times(1)).send(eventCaptor.capture());

        TransferEvent event = eventCaptor.getValue();
        assertEquals(account1.id(), event.fromAccountId());
        assertEquals(account2.id(), event.toAccountId());
    }

    @Test
    void shouldNotifyTransferWithZeroAmount() {
        BigDecimal zeroAmount = BigDecimal.ZERO;

        service.notifyTransfer(fromAccount, toAccount, zeroAmount);

        ArgumentCaptor<TransferEvent> eventCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        verify(producer, times(1)).send(eventCaptor.capture());

        TransferEvent event = eventCaptor.getValue();
        assertEquals(zeroAmount, event.amount());
    }

    @Test
    void shouldCallProducerOnce() {
        service.notifyTransfer(fromAccount, toAccount, amount);

        verify(producer, times(1)).send(new TransferEvent(fromAccount.id(), toAccount.id(), amount));
    }
}

