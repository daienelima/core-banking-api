package core.banking.api.infrastructure.persistence.repository;

import core.banking.api.domain.repository.TransactionRepository;
import core.banking.api.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepositoryImpl implements TransactionRepository {

    private final SpringDataTransactionRepository repository;

    public TransactionRepositoryImpl(SpringDataTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(core.banking.api.domain.model.Transaction transaction) {
        TransactionEntity entity = new TransactionEntity();
        entity.setFromAccountId(transaction.fromAccountId());
        entity.setToAccountId(transaction.toAccountId());
        entity.setAmount(transaction.amount());
        entity.setCreatedAt(transaction.createdAt());

        repository.save(entity);
    }
}
