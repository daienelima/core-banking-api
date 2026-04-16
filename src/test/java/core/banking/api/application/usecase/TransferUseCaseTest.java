package core.banking.api.application.usecase;

import core.banking.api.domain.model.Account;
import core.banking.api.domain.model.Transaction;
import core.banking.api.domain.repository.AccountRepository;
import core.banking.api.domain.repository.TransactionRepository;
import core.banking.api.domain.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransferUseCase useCase;

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setup() {
        fromAccount = mock(Account.class);
        toAccount = mock(Account.class);
    }

    @Test
    void shouldTransferMoneySuccessfully() {
        Long fromId = 1L;
        Long toId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100);

        when(accountRepository.findByIdForUpdate(fromId)).thenReturn(fromAccount);
        when(accountRepository.findByIdForUpdate(toId)).thenReturn(toAccount);

        useCase.execute(fromId, toId, amount);

        verify(fromAccount).debit(amount);
        verify(toAccount).credit(amount);

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);

        verify(transactionRepository).save(any(Transaction.class));

        verify(notificationService).notifyTransfer(fromAccount, toAccount, amount);
    }

}