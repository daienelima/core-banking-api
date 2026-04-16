package core.banking.api.infrastructure.persistence.repository;

import core.banking.api.domain.model.Transaction;
import core.banking.api.infrastructure.persistence.entity.TransactionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionRepositoryImplTest {

    @Mock
    private SpringDataTransactionRepository repository;

    @InjectMocks
    private TransactionRepositoryImpl transactionRepository;

    private Transaction transaction;

    @BeforeEach
    void setup() {
        transaction = new Transaction(1L, 2L, new BigDecimal("100.00"));
    }

    @Test
    void shouldSaveTransactionSuccessfully() {
        transactionRepository.save(transaction);

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        TransactionEntity capturedEntity = entityCaptor.getValue();
        assertEquals(transaction.fromAccountId(), capturedEntity.getFromAccountId());
        assertEquals(transaction.toAccountId(), capturedEntity.getToAccountId());
        assertEquals(transaction.amount(), capturedEntity.getAmount());
    }

    @Test
    void shouldMapFromAccountIdCorrectly() {
        transactionRepository.save(transaction);

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(1L, entityCaptor.getValue().getFromAccountId());
    }

    @Test
    void shouldMapToAccountIdCorrectly() {
        transactionRepository.save(transaction);

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(2L, entityCaptor.getValue().getToAccountId());
    }

    @Test
    void shouldMapAmountCorrectly() {
        transactionRepository.save(transaction);

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(new BigDecimal("100.00"), entityCaptor.getValue().getAmount());
    }


    @Test
    void shouldSaveTransactionWithLargeAmount() {
        Transaction largeAmountTransaction = new Transaction(1L, 2L, new BigDecimal("999999.99"));

        transactionRepository.save(largeAmountTransaction);

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(new BigDecimal("999999.99"), entityCaptor.getValue().getAmount());
    }

    @Test
    void shouldSaveTransactionWithSmallAmount() {
        Transaction smallAmountTransaction = new Transaction(1L, 2L, new BigDecimal("0.01"));

        transactionRepository.save(smallAmountTransaction);

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(new BigDecimal("0.01"), entityCaptor.getValue().getAmount());
    }

    @Test
    void shouldSaveTransactionWithZeroAmount() {
        Transaction zeroAmountTransaction = new Transaction(1L, 2L, BigDecimal.ZERO);

        transactionRepository.save(zeroAmountTransaction);

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(BigDecimal.ZERO, entityCaptor.getValue().getAmount());
    }

    @Test
    void shouldSaveTransactionBetweenSameAccounts() {
        Transaction sameAccountTransaction = new Transaction(1L, 1L, new BigDecimal("100.00"));

        transactionRepository.save(sameAccountTransaction);

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(1L, entityCaptor.getValue().getFromAccountId());
        assertEquals(1L, entityCaptor.getValue().getToAccountId());
    }

    @Test
    void shouldSaveMultipleTransactionsSequentially() {
        Transaction transaction1 = new Transaction(1L, 2L, new BigDecimal("100.00"));
        Transaction transaction2 = new Transaction(3L, 4L, new BigDecimal("200.00"));

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        verify(repository, times(2)).save(any(TransactionEntity.class));
    }

    @Test
    void shouldCallRepositorySaveOncePerTransaction() {
        transactionRepository.save(transaction);

        verify(repository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    void shouldSaveTransactionWithDifferentAccountIds() {
        Transaction differentIdsTransaction = new Transaction(100L, 200L, new BigDecimal("100.00"));

        transactionRepository.save(differentIdsTransaction);

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(100L, entityCaptor.getValue().getFromAccountId());
        assertEquals(200L, entityCaptor.getValue().getToAccountId());
    }
}

