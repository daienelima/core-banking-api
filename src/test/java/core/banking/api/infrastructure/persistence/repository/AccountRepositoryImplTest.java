package core.banking.api.infrastructure.persistence.repository;

import core.banking.api.domain.exception.ContaNaoEncontradaException;
import core.banking.api.domain.model.Account;
import core.banking.api.infrastructure.persistence.entity.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryImplTest {

    @Mock
    private SpringDataAccountRepository repository;

    @InjectMocks
    private AccountRepositoryImpl accountRepository;

    private AccountEntity accountEntity;
    private Account account;

    @BeforeEach
    void setup() {
        accountEntity = new AccountEntity();
        accountEntity.setId(1L);
        accountEntity.setName("John Doe");
        accountEntity.setBalance(new BigDecimal("1000.00"));

        account = new Account(1L, "John Doe", new BigDecimal("1000.00"));
    }

    @Test
    void shouldFindAccountByIdSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(accountEntity));

        Account result = accountRepository.findById(1L);

        assertEquals(account.id(), result.id());
        assertEquals(account.name(), result.name());
        assertEquals(account.balance(), result.balance());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundById() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> accountRepository.findById(999L));
    }

    @Test
    void shouldFindAccountByIdForUpdateSuccessfully() {
        when(repository.findByIdForUpdate(1L)).thenReturn(accountEntity);

        Account result = accountRepository.findByIdForUpdate(1L);

        assertEquals(account.id(), result.id());
        assertEquals(account.name(), result.name());
        assertEquals(account.balance(), result.balance());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundByIdForUpdate() {
        when(repository.findByIdForUpdate(999L)).thenReturn(null);

        assertThrows(ContaNaoEncontradaException.class, () -> accountRepository.findByIdForUpdate(999L));
    }

    @Test
    void shouldSaveAccountSuccessfully() {
        accountRepository.save(account);

        ArgumentCaptor<AccountEntity> entityCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        AccountEntity capturedEntity = entityCaptor.getValue();
        assertEquals(account.id(), capturedEntity.getId());
        assertEquals(account.name(), capturedEntity.getName());
        assertEquals(account.balance(), capturedEntity.getBalance());
    }

    @Test
    void shouldFindAccountWithDifferentBalance() {
        AccountEntity entityWithDifferentBalance = new AccountEntity();
        entityWithDifferentBalance.setId(1L);
        entityWithDifferentBalance.setName("John Doe");
        entityWithDifferentBalance.setBalance(new BigDecimal("5000.50"));

        when(repository.findById(1L)).thenReturn(Optional.of(entityWithDifferentBalance));

        Account result = accountRepository.findById(1L);

        assertEquals(new BigDecimal("5000.50"), result.balance());
    }

    @Test
    void shouldFindAccountWithDifferentName() {
        AccountEntity entityWithDifferentName = new AccountEntity();
        entityWithDifferentName.setId(1L);
        entityWithDifferentName.setName("Jane Doe");
        entityWithDifferentName.setBalance(new BigDecimal("1000.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(entityWithDifferentName));

        Account result = accountRepository.findById(1L);

        assertEquals("Jane Doe", result.name());
    }

    @Test
    void shouldSaveAccountWithZeroBalance() {
        Account accountWithZeroBalance = new Account(1L, "John Doe", BigDecimal.ZERO);

        accountRepository.save(accountWithZeroBalance);

        ArgumentCaptor<AccountEntity> entityCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(BigDecimal.ZERO, entityCaptor.getValue().getBalance());
    }

    @Test
    void shouldSaveAccountWithLargeBalance() {
        Account accountWithLargeBalance = new Account(1L, "John Doe", new BigDecimal("999999.99"));

        accountRepository.save(accountWithLargeBalance);

        ArgumentCaptor<AccountEntity> entityCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        assertEquals(new BigDecimal("999999.99"), entityCaptor.getValue().getBalance());
    }

    @Test
    void shouldFindMultipleAccountsWithDifferentIds() {
        AccountEntity entity1 = new AccountEntity();
        entity1.setId(1L);
        entity1.setName("Account 1");
        entity1.setBalance(new BigDecimal("1000.00"));

        AccountEntity entity2 = new AccountEntity();
        entity2.setId(2L);
        entity2.setName("Account 2");
        entity2.setBalance(new BigDecimal("2000.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(repository.findById(2L)).thenReturn(Optional.of(entity2));

        Account result1 = accountRepository.findById(1L);
        Account result2 = accountRepository.findById(2L);

        assertEquals(1L, result1.id());
        assertEquals(2L, result2.id());
    }

    @Test
    void shouldCallRepositoryFindByIdOnce() {
        when(repository.findById(1L)).thenReturn(Optional.of(accountEntity));

        accountRepository.findById(1L);

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldCallRepositoryFindByIdForUpdateOnce() {
        when(repository.findByIdForUpdate(1L)).thenReturn(accountEntity);

        accountRepository.findByIdForUpdate(1L);

        verify(repository, times(1)).findByIdForUpdate(1L);
    }

    @Test
    void shouldCallRepositorySaveOnce() {
        accountRepository.save(account);

        verify(repository, times(1)).save(any(AccountEntity.class));
    }
}

