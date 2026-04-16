package core.banking.api.infrastructure.persistence.repository;


import core.banking.api.domain.exception.ContaNaoEncontradaException;
import core.banking.api.domain.model.Account;
import core.banking.api.domain.repository.AccountRepository;
import core.banking.api.infrastructure.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryImpl implements AccountRepository {

    private final SpringDataAccountRepository repository;

    public AccountRepositoryImpl(SpringDataAccountRepository repository) {
        this.repository = repository;
    }

    public Account findById(Long id) {
        AccountEntity entity = repository.findById(id)
                .orElseThrow(ContaNaoEncontradaException::new);

        return toDomain(entity);
    }

    public Account findByIdForUpdate(Long id) {
        AccountEntity entity = repository.findByIdForUpdate(id);

        if (entity == null) {
            throw new ContaNaoEncontradaException();
        }
        return toDomain(entity);
    }

    public void save(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.setId(account.id());
        entity.setName(account.name());
        entity.setBalance(account.balance());

        repository.save(entity);
    }

    private Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getName(),
                entity.getBalance());
    }
}